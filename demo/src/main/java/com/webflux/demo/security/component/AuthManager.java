package com.webflux.demo.security.component;

import com.webflux.demo.security.domain.JwtToken;
import com.webflux.demo.security.exception.JwtAuthenticationException;
import com.webflux.demo.security.filter.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
@Log
@Component
@RequiredArgsConstructor
public class AuthManager implements ReactiveAuthenticationManager {



    private final TokenProvider tokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .cast(JwtToken.class)
                .filter(jwtToken -> tokenProvider.isTokenValid(jwtToken.getToken()))
                .map(jwtToken -> jwtToken.withAuthenticated(true))
                .switchIfEmpty(Mono.error(new JwtAuthenticationException("Invalid token.")));
    }

}
