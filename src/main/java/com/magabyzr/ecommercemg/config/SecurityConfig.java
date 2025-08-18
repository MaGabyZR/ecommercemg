package com.magabyzr.ecommercemg.config;

import com.magabyzr.ecommercemg.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //Hashing passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Register DaoAuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    //Register a Bean as the default implementation of AuthenticationManager.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

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
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()                           //d. allow access to the login API.
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()                         //e. allow request to refresh tokens.
                        .anyRequest().authenticated()                                                            //Any other request should be authenticated.
                )
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   //order of how the filters should be called.
                        .exceptionHandling(c ->
                                c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build();

    }
}
