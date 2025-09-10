package com.magabyzr.ecommercemg.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping                                                                                         //to expose it, it is the same as RequestMapping
    public Iterable<UserDto> getAllUsers(                                                                  //Iterable is the parent of List<>
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy                //Set it to false, to make it optional.
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {                                                         //to pass id dynamically to this route.
        return userService.getUser(id);
    }
    //to accept POST requests to register a new User.
    @PostMapping
    public ResponseEntity<?> registerUser(                                                                  //use a ? to make the method more flexible and be able to use a map.
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder                                                                 //to access the id of the new user and get a 201 status in Postman.
            ){

        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();                   //to map the path to the user id.

        return ResponseEntity.created(uri).body(userDto);                                                   //to return by the id of the user.
    }
    //to update a resource
    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request){

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request){
        userService.changePassword(id, request);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser() {
        return ResponseEntity.badRequest().body(
                Map.of("email", "Email is already registered.")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
