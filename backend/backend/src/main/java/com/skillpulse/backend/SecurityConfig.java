package com.skillpulse.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final FirebaseSecurityFilter firebaseSecurityFilter;

    public SecurityConfig(FirebaseSecurityFilter firebaseSecurityFilter) {
        this.firebaseSecurityFilter = firebaseSecurityFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(firebaseSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}