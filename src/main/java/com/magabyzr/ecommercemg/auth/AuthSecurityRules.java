package com.magabyzr.ecommercemg.auth;

import com.magabyzr.ecommercemg.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()                            //a. allow access to the login API.
                .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll();                         //b. allow request to our webhook.
    }
}
