package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.JwtResponse;
import com.magabyzr.ecommercemg.dtos.LoginRequest;
import com.magabyzr.ecommercemg.dtos.UserDto;
import com.magabyzr.ecommercemg.mappers.UserMapper;
import com.magabyzr.ecommercemg.repositories.UserRepository;
import com.magabyzr.ecommercemg.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    //private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        /*find a user with a given email. Replaced with Authentication Manager.
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //validate the pw.
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        //Call the user repository to fetch the user object.
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        //generate the token.
        var token = jwtService.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token));
    }
    //Validating JSON Web Tokens.
    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader){
        System.out.println(("Validate called."));

        var token = authHeader.replace("Bearer ", "");

        return jwtService.validateToken(token);
    }
    //Endpoint for getting the current user.
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        //Extracting the current principal(user).
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        //Find the user in the repository.
        var user = userRepository.findById(userId).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        //Map the user
        var userDto = userMapper.toDto(user);

        //Return the result.
        return ResponseEntity.ok(userDto);
    }

    //Exception handler.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
