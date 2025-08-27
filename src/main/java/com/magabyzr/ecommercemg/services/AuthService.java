package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.entities.User;
import com.magabyzr.ecommercemg.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//centralize the access to the current user.
@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        //Extracting the current principal(user).
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        //Find the user in the repository and return it.
        return userRepository.findById(userId).orElse(null);
    }

}
