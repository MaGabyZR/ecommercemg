package com.magabyzr.ecommercemg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {                          //it defines how HTTP requests are secured.
        //1. Stateless sessions(token-based authentication)
        //2.Disable CSRF
        //3.Authorize, define what endpoints are public or private.
        http
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        //.anyRequest().permitAll()                                                              //a. all endpoints are public.
                        .requestMatchers("/carts/**").permitAll()                                              //b. make carts public.
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()                                //c. allow users to register without being authenticated first.
                        .anyRequest().authenticated()                                                            //Any other request should be authenticated.

                );
        return http.build();

    }
}
