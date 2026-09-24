package com.pengwingscorp.halo.security.service;

import com.pengwingscorp.halo.user.infrastructure.UserRepository;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaEntity;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        UserJpaEntity userJpaEntity = userRepository.findByUsername(username);
        return UserJpaMapper.toDomain(userJpaEntity);
    }
}
