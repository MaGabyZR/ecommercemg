package com.magabyzr.ecommercemg.auth;

import com.magabyzr.ecommercemg.users.UserDto;
import com.magabyzr.ecommercemg.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final AuthService authService;


    @PostMapping("/login")
    public JwtResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        var loginResult = authService.login(request);

        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);                                                                       //it cannot be accessed by JavaScript.
        cookie.setPath("/auth/refresh");                                                                //set the cookie path. "/" means all webpage.
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());                                                               //expires after 7 days.
        cookie.setSecure(true);                                                                         //make it a secure cookie, it will only be sent over HTTPS connections.
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());                                 //the access token is returned in the body of the response.
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

    //Endpoint for refreshing and access token.
    @PostMapping("/refresh")
    public JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    //Endpoint for getting the current user.
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        //Extracting the current user.
        var user = authService.getCurrentUser();
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
