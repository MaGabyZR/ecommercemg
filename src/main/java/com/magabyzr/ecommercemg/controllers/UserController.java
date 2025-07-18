package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.RegisterUserRequest;
import com.magabyzr.ecommercemg.dtos.UpdateUserRequest;
import com.magabyzr.ecommercemg.dtos.UserDto;
import com.magabyzr.ecommercemg.mappers.UserMapper;
import com.magabyzr.ecommercemg.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping                                                                                         //to expose it, it is the same as RequestMapping
    public Iterable<UserDto> getAllUsers(                                                                  //Iterable is the parent of List<>
            //@RequestHeader(required = false, name = "x-auth-token") String authToken,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort                //Set it to false, to make it optional.
    ) {
        //System.out.println(authToken);

        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return userRepository.findAll(Sort.by(sort))
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

    //to accept POST requests.
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder                                                             //to access the id of the new user and get a 201 status in Postman.
            ){
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();               //to map the path to the user id.

        return ResponseEntity.created(uri).body(userDto);                                               //to return by the id of the user.
    }
    //to update a resource
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request){
            var user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            userMapper.update(request,user);
            userRepository.save(user);

            return ResponseEntity.ok(userMapper.toDto(user));
    }
}
