package com.pengwingscorp.halo.user.application;

import com.pengwingscorp.halo.security.infra.TokenService;
import com.pengwingscorp.halo.user.domain.User;
import com.pengwingscorp.halo.user.infrastructure.UserRepository;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaEntity;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaMapper;
import com.pengwingscorp.halo.user.web.dto.UserRequest;
import com.pengwingscorp.halo.user.web.dto.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public RegisterUser(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public UserResponse execute(UserRequest userRequest) {
        if(userRepository.findByUsername(userRequest.login()) != null) throw new RuntimeException("Use a different username");

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequest.password());
        User newUser = new User(userRequest.login(), encryptedPassword, userRequest.role());

        UserJpaEntity savedUser = userRepository.save(UserJpaMapper.toDatabase(newUser));
        return new UserResponse(
                savedUser.getUsername(),
                savedUser.getPassword(),
                savedUser.getRole()
        );
    }
}
