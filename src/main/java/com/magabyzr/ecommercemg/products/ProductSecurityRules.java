package com.magabyzr.ecommercemg.products;

import com.magabyzr.ecommercemg.common.SecurityRules;
import com.magabyzr.ecommercemg.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET, "/products/**").permitAll()                           //a. allow access to the all products endpoint including the children.
                .requestMatchers(HttpMethod.POST, "/products/**").hasRole(Role.ADMIN.name())           //b. only admins can post, put or delete.
                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole(Role.ADMIN.name());
    }
}
