package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.UserDto;
import com.magabyzr.ecommercemg.mappers.UserMapper;
import com.magabyzr.ecommercemg.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping                                                     //to expose it, it is the same as RequestMapping
    public Iterable<UserDto> getAllUsers() {                           //Iterable is the parent of List<>
        return userRepository.findAll()
                .stream()                                              //From here start using Dto
                .map(userMapper::toDto)                                //Replacing the old lambda function with a call to the method.
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {            //to pass id dynamically to this route.
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
