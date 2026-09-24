package com.pengwingscorp.halo.security.infra;

import com.pengwingscorp.halo.security.exception.UserNotFound;
import com.pengwingscorp.halo.user.domain.User;
import com.pengwingscorp.halo.user.infrastructure.UserRepository;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaEntity;
import com.pengwingscorp.halo.user.infrastructure.persistence.UserJpaMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = recoverToken(request);
            handleToken(token);
        } catch (JwtException | UserNotFound e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleToken(String token) {
        if(token == null) return;
        String login = tokenService.extractUsername(token);
        if (login == null || login.isBlank()) {
            throw new JwtException("Missing token subject");
        }

        UserJpaEntity userJpaEntity = userRepository.findByUsername(login);
        if(userJpaEntity == null) throw new UserNotFound();
        User user = UserJpaMapper.toDomain(userJpaEntity);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String recoverToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header == null
                || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return null;
        }
        String token = header.replace("Bearer ", "");
        if (token.isEmpty()) {
            throw new JwtException("Empty bearer token");
        }
        return token;
    }
}
