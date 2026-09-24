package com.pengwingscorp.halo.user.infrastructure.persistence;

import com.pengwingscorp.halo.user.domain.User;
import com.pengwingscorp.halo.user.domain.UserRole;

public class UserJpaMapper {
    public static User toDomain(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getUsername(),
                userJpaEntity.getPassword(),
                UserRole.valueOf(userJpaEntity.getRole())
        );
    }

    public static UserJpaEntity toDatabase(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
