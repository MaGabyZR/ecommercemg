package com.magabyzr.ecommercemg.users;

import com.magabyzr.ecommercemg.dtos.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    //to accept POST requests to register a new User.
    @PostMapping
    public ResponseEntity<?> registerUser(                                                              //use a ? to make the method more flexible and be able to use a map.
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder                                                             //to access the id of the new user and get a 201 status in Postman.
            ){
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Email is already registered!")
            );
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request){

        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
