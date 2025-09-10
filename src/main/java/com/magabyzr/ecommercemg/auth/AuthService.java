package com.magabyzr.ecommercemg.auth;

import com.magabyzr.ecommercemg.users.User;
import com.magabyzr.ecommercemg.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//centralize the access to the current user.
@AllArgsConstructor
@Service
public class AuthService {
    //private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User getCurrentUser(){
        //Extracting the current principal(user).
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        //Find the user in the repository and return it.
        return userRepository.findById(userId).orElse(null);
    }

    //Handle all the login logic.
    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //Call the user repository to fetch the user object.
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        //generate the accessToken and refreshToken.
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }
    //Validate refresh token.
    public Jwt refreshAccessToken(String refreshToken){
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            throw new BadCredentialsException("Invalid refresh token");
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return  jwtService.generateAccessToken(user);
    }
}


        /*find a user with a given email. Replaced with Authentication Manager.
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //validate the pw.
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/

