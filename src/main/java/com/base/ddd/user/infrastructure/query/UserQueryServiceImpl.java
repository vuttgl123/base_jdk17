package com.base.ddd.user.infrastructure.query;

import com.base.ddd.shared.infrastructure.PageResponse;
import com.base.ddd.shared.infrastructure.SearchRequest;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.dto.UserQuery;
import com.base.ddd.user.domain.model.UserStatus;
import com.base.ddd.user.infrastructure.persistence.entity.QUserJpaEntity;
import com.base.ddd.user.infrastructure.persistence.entity.UserJpaEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User Query Service Implementation
 * Uses QueryDSL for flexible queries
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> search(SearchRequest searchRequest) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.deleted.eq(false));

        // Search by keyword in multiple fields
        if (searchRequest.hasKeyword()) {
            String keyword = searchRequest.getKeyword().toLowerCase();
            BooleanBuilder keywordBuilder = new BooleanBuilder();

            if (searchRequest.hasSearchFields()) {
                for (String field : searchRequest.getSearchFields()) {
                    switch (field) {
                        case "username" -> keywordBuilder.or(qUser.username.containsIgnoreCase(keyword));
                        case "email" -> keywordBuilder.or(qUser.email.containsIgnoreCase(keyword));
                        case "fullName" -> keywordBuilder.or(qUser.fullName.containsIgnoreCase(keyword));
                        case "phoneNumber" -> keywordBuilder.or(qUser.phoneNumber.containsIgnoreCase(keyword));
                    }
                }
            } else {
                // Default search fields
                keywordBuilder.or(qUser.username.containsIgnoreCase(keyword))
                        .or(qUser.email.containsIgnoreCase(keyword))
                        .or(qUser.fullName.containsIgnoreCase(keyword));
            }

            builder.and(keywordBuilder);
        }

        // Count total
        long total = queryFactory
                .selectFrom(qUser)
                .where(builder)
                .fetchCount();

        // Build query
        JPAQuery<UserJpaEntity> query = queryFactory
                .selectFrom(qUser)
                .where(builder);

        // Apply sorting
        List<OrderSpecifier<?>> orders = buildOrderSpecifiers(searchRequest, qUser);
        for (OrderSpecifier<?> order : orders) {
            query.orderBy(order);
        }

        // Apply pagination
        List<UserJpaEntity> entities = query
                .offset((long) searchRequest.getValidPage() * searchRequest.getValidSize())
                .limit(searchRequest.getValidSize())
                .fetch();

        // Convert to DTO
        List<UserDTO> content = entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return PageResponse.of(
                new org.springframework.data.domain.PageImpl<>(
                        content,
                        PageRequest.of(searchRequest.getValidPage(), searchRequest.getValidSize()),
                        total
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> filter(UserQuery query) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();

        // Filter deleted
        if (query.getDeleted() != null) {
            builder.and(qUser.deleted.eq(query.getDeleted()));
        } else {
            builder.and(qUser.deleted.eq(false));
        }

        // Filter by username
        if (query.getUsername() != null && !query.getUsername().isBlank()) {
            builder.and(qUser.username.containsIgnoreCase(query.getUsername()));
        }

        // Filter by email
        if (query.getEmail() != null && !query.getEmail().isBlank()) {
            builder.and(qUser.email.containsIgnoreCase(query.getEmail()));
        }

        // Filter by full name
        if (query.getFullName() != null && !query.getFullName().isBlank()) {
            builder.and(qUser.fullName.containsIgnoreCase(query.getFullName()));
        }

        // Filter by status
        if (query.getStatus() != null && !query.getStatus().isBlank()) {
            builder.and(qUser.status.eq(query.getStatus()));
        }

        // Filter by age range
        if (query.getMinAge() != null) {
            builder.and(qUser.age.goe(query.getMinAge()));
        }
        if (query.getMaxAge() != null) {
            builder.and(qUser.age.loe(query.getMaxAge()));
        }

        // Filter by phone
        if (query.getPhoneNumber() != null && !query.getPhoneNumber().isBlank()) {
            builder.and(qUser.phoneNumber.containsIgnoreCase(query.getPhoneNumber()));
        }

        // Filter by created by
        if (query.getCreatedBy() != null && !query.getCreatedBy().isBlank()) {
            builder.and(qUser.createdBy.eq(query.getCreatedBy()));
        }

        // Filter by updated by
        if (query.getUpdatedBy() != null && !query.getUpdatedBy().isBlank()) {
            builder.and(qUser.updatedBy.eq(query.getUpdatedBy()));
        }

        // Search by keyword
        if (query.hasKeyword()) {
            String keyword = query.getKeyword().toLowerCase();
            builder.and(
                    qUser.username.containsIgnoreCase(keyword)
                            .or(qUser.email.containsIgnoreCase(keyword))
                            .or(qUser.fullName.containsIgnoreCase(keyword))
            );
        }

        // Count total
        long total = queryFactory
                .selectFrom(qUser)
                .where(builder)
                .fetchCount();

        // Build query
        JPAQuery<UserJpaEntity> query2 = queryFactory
                .selectFrom(qUser)
                .where(builder);

        // Apply sorting
        List<OrderSpecifier<?>> orders = buildOrderSpecifiers(query, qUser);
        for (OrderSpecifier<?> order : orders) {
            query2.orderBy(order);
        }

        // Apply pagination
        List<UserJpaEntity> entities = query2
                .offset((long) query.getValidPage() * query.getValidSize())
                .limit(query.getValidSize())
                .fetch();

        // Convert to DTO
        List<UserDTO> content = entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return PageResponse.of(
                new org.springframework.data.domain.PageImpl<>(
                        content,
                        PageRequest.of(query.getValidPage(), query.getValidSize()),
                        total
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> searchByKeyword(String keyword) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        List<UserJpaEntity> entities = queryFactory
                .selectFrom(qUser)
                .where(qUser.deleted.eq(false)
                        .and(qUser.fullName.containsIgnoreCase(keyword)
                                .or(qUser.email.containsIgnoreCase(keyword))
                                .or(qUser.username.containsIgnoreCase(keyword))))
                .orderBy(qUser.createdAt.desc())
                .fetch();

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        UserJpaEntity entity = queryFactory
                .selectFrom(qUser)
                .where(qUser.email.eq(email).and(qUser.deleted.eq(false)))
                .fetchOne();

        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        UserJpaEntity entity = queryFactory
                .selectFrom(qUser)
                .where(qUser.username.eq(username).and(qUser.deleted.eq(false)))
                .fetchOne();

        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findByStatus(UserStatus status) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        List<UserJpaEntity> entities = queryFactory
                .selectFrom(qUser)
                .where(qUser.status.eq(status.name()).and(qUser.deleted.eq(false)))
                .orderBy(qUser.createdAt.desc())
                .fetch();

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(UserStatus status) {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        return queryFactory
                .selectFrom(qUser)
                .where(qUser.status.eq(status.name()).and(qUser.deleted.eq(false)))
                .fetchCount();
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotal() {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        return queryFactory
                .selectFrom(qUser)
                .where(qUser.deleted.eq(false))
                .fetchCount();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        QUserJpaEntity qUser = QUserJpaEntity.userJpaEntity;

        return queryFactory
                .selectFrom(qUser)
                .where(qUser.status.eq("ACTIVE").and(qUser.deleted.eq(false)))
                .fetchCount();
    }

    private List<OrderSpecifier<?>> buildOrderSpecifiers(SearchRequest request, QUserJpaEntity qUser) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (request.getSorts() != null && !request.getSorts().isEmpty()) {
            for (SearchRequest.SortRequest sort : request.getSorts()) {
                OrderSpecifier<?> order = buildOrderSpecifier(sort.getField(),
                        sort.getDirection() != null && sort.getDirection() == org.springframework.data.domain.Sort.Direction.DESC,
                        qUser);
                if (order != null) {
                    orders.add(order);
                }
            }
        }

        if (orders.isEmpty()) {
            orders.add(qUser.createdAt.desc());
        }

        return orders;
    }

    private OrderSpecifier<?> buildOrderSpecifier(String field, boolean desc, QUserJpaEntity qUser) {
        return switch (field) {
            case "id" -> desc ? qUser.id.desc() : qUser.id.asc();
            case "username" -> desc ? qUser.username.desc() : qUser.username.asc();
            case "email" -> desc ? qUser.email.desc() : qUser.email.asc();
            case "fullName" -> desc ? qUser.fullName.desc() : qUser.fullName.asc();
            case "status" -> desc ? qUser.status.desc() : qUser.status.asc();
            case "age" -> desc ? qUser.age.desc() : qUser.age.asc();
            case "createdAt" -> desc ? qUser.createdAt.desc() : qUser.createdAt.asc();
            case "updatedAt" -> desc ? qUser.updatedAt.desc() : qUser.updatedAt.asc();
            default -> null;
        };
    }

    private UserDTO toDTO(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .age(entity.getAge())
                .address(entity.getAddress())
                .avatarUrl(entity.getAvatarUrl())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }
}