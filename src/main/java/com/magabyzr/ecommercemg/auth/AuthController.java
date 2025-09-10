package com.magabyzr.ecommercemg.auth;

import com.magabyzr.ecommercemg.users.UserDto;
import com.magabyzr.ecommercemg.users.UserMapper;
import com.magabyzr.ecommercemg.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
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
        //generate the accessToken and refreshToken.
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);                                                                //it cannot be accessed by JavaScript.
        cookie.setPath("/auth/refresh");                                                         //set the cookie path. "/" means all webpage.
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());                                                               //expires after 7 days.
        cookie.setSecure(true);                                                                 //make it a secure cookie, it will only be sent over HTTPS connections.
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));                                 //the access token is returned in the body of the response.

    }
    //Validating JSON Web Tokens.
//    @PostMapping("/validate")
//    public boolean validate(@RequestHeader("Authorization") String authHeader){
//        System.out.println(("Validate called."));
//
//        var token = authHeader.replace("Bearer ", "");
//
//        return jwtService.validateToken(token);
//    }

    //Endpoint for refreshing and access toke.
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            //receive the refresh token.
            @CookieValue(value = "refreshToken") String refreshToken
    ){
        //validate the refresh token.
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
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
