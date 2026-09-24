package com.pengwingscorp.halo.user.infrastructure;

import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, UUID> {
    UserJpaEntity findByUsername(String login);
}
