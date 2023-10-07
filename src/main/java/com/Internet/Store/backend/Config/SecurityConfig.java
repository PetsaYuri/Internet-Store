package com.Internet.Store.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(form -> form.
                        loginProcessingUrl("/login")
                        .defaultSuccessUrl("/profile")
                        .failureUrl("/login?error"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                        .requestMatchers(("/api/users/**")).hasAnyRole("admin", "chief")
                        .requestMatchers(HttpMethod.GET, "/api/orders/myOrders").authenticated()
                        .requestMatchers(("/api/orders/**")).hasAnyRole("admin", "chief")
                        .requestMatchers(HttpMethod.POST, "/api/items/{id}/buy").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()
                        .requestMatchers(("/api/items/**")).hasAnyRole("admin", "chief")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(("/api/categories/**")).hasAnyRole("admin", "chief")
                        .requestMatchers(("/api/profile")).authenticated()
                        .requestMatchers(("/api/receiveOrders")).authenticated()
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
