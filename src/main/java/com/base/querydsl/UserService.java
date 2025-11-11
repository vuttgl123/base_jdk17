package com.base.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService extends GenericService<User, UserCreateRequest, UserUpdateRequest, UserResponse> {

    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       JPAQueryFactory queryFactory,
                       PasswordEncoder passwordEncoder) {
        super(repository, mapper, new DynamicQueryBuilder<>(User.class));
        this.userRepository = repository;
        this.queryFactory = queryFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void beforeCreate(User entity, UserCreateRequest request) {
        // Hash password
        entity.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // Validate unique email
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Validate unique username
        if (userRepository.existsByUsernameAndDeletedFalse(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
    }

    // ========== Custom methods sử dụng QUser ==========

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        QUser qUser = QUser.user;

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.email.eq(email)
                        .and(qUser.deleted.eq(false)))
                .fetchOne();

        return Optional.ofNullable(user).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByUsername(String username) {
        QUser qUser = QUser.user;

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.username.eq(username)
                        .and(qUser.deleted.eq(false)))
                .fetchOne();

        return Optional.ofNullable(user).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> searchByKeyword(String keyword) {
        QUser qUser = QUser.user;

        List<User> users = queryFactory
                .selectFrom(qUser)
                .where(qUser.deleted.eq(false)
                        .and(qUser.fullName.containsIgnoreCase(keyword)
                                .or(qUser.email.containsIgnoreCase(keyword))
                                .or(qUser.username.containsIgnoreCase(keyword))))
                .orderBy(qUser.createdAt.desc())
                .fetch();

        return mapper.toResponseList(users);
    }
}
