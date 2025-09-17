package com.magabyzr.ecommercemg.admin;

import com.magabyzr.ecommercemg.common.SecurityRules;
import com.magabyzr.ecommercemg.users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());                               //to restrict access to admin only.
    }
}
