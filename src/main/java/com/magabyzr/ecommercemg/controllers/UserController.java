package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.entities.User;
import com.magabyzr.ecommercemg.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping                                 //to expose it, it is the same as RequestMapping
    public Iterable<User> getAllUsers() {                   //Iterable is the parent of List<>
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {            //to pass id dynamically to this route.
        return userRepository.findById(id).orElse(null);
    }
}
