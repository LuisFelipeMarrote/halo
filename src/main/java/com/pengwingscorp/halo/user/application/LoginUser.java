package com.pengwingscorp.halo.user.application;

import com.pengwingscorp.halo.security.infra.TokenService;
import com.pengwingscorp.halo.user.domain.User;
import com.pengwingscorp.halo.user.web.dto.AuthenticationDTO;
import com.pengwingscorp.halo.user.web.dto.TokenDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginUser {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginUser(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public TokenDTO execute(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new TokenDTO(token);
    }
}
