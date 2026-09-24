package com.pengwingscorp.halo.user.web;

import com.pengwingscorp.halo.security.infra.TokenService;
import com.pengwingscorp.halo.user.application.LoginUser;
import com.pengwingscorp.halo.user.application.RegisterUser;
import com.pengwingscorp.halo.user.web.dto.AuthenticationDTO;
import com.pengwingscorp.halo.user.web.dto.UserRequest;
import com.pengwingscorp.halo.user.web.dto.TokenDTO;
import com.pengwingscorp.halo.user.domain.User;
import com.pengwingscorp.halo.user.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class UserController {

    private final LoginUser loginUser;
    private final RegisterUser registerUser;

    public UserController(LoginUser loginUser, RegisterUser registerUser) {
        this.loginUser = loginUser;
        this.registerUser = registerUser;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDTO login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        return loginUser.execute(authenticationDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid UserRequest userRequest){
        return registerUser.execute(userRequest);
    }
}
