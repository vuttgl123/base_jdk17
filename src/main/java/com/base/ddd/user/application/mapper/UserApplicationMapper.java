package com.base.ddd.user.application.mapper;

import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper between Domain Model and Application DTO
 */
@Component
public class UserApplicationMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId() != null ? user.getId().getValue() : null)
                .username(user.getUsername() != null ? user.getUsername().getValue() : null)
                .email(user.getEmail() != null ? user.getEmail().getValue() : null)
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .age(user.getAge())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .version(user.getVersion())
                .deleted(user.isDeleted())
                .build();
    }

    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> toDTOOptional(Optional<User> userOptional) {
        return userOptional.map(this::toDTO);
    }

    public Page<UserDTO> toDTOPage(Page<User> userPage) {
        return userPage.map(this::toDTO);
    }

    /**
     * Map to DTO excluding sensitive information (for public API)
     */
    public UserDTO toPublicDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId() != null ? user.getId().getValue() : null)
                .username(user.getUsername() != null ? user.getUsername().getValue() : null)
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    /**
     * Map to summary DTO (minimal information)
     */
    public UserDTO toSummaryDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId() != null ? user.getId().getValue() : null)
                .username(user.getUsername() != null ? user.getUsername().getValue() : null)
                .fullName(user.getFullName())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}