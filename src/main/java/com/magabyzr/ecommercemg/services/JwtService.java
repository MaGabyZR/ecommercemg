package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.config.JwtConfig;
import com.magabyzr.ecommercemg.entities.Role;
import com.magabyzr.ecommercemg.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
/*    @Value("${spring.jwt.secret}")            //replaced with a Jwt Config object
    private String secret;*/

    //Inject our secret with a Jwt Config Object.
    private final JwtConfig jwtConfig;

    //Generate access tokens.
    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    //Generate refresh tokens.
    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))                  //*1000 because you are dealing with milliseconds.
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    //Validate tokens. Replaced with Jwt.java
//    public boolean validateToken(String token){
//        try{
//            var claims = getClaims(token);
//            return claims.getExpiration().after(new Date());
//        }
//        catch(JwtException ex){
//            return false;
//        }
//    }
    //Parse the token
    public Jwt parseToken(String token){
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }
    //reusable method for extracting the claims / payload.
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Get the id from a token. Replaced with Jwt.java
//    public Long getUserIdFromToken(String token){
//        return Long.valueOf(getClaims(token).getSubject());
//    }
    //Get the role from the token. Replaced with Jwt.java
//    public Role getRoleFromToken(String token){
//        return Role.valueOf(getClaims(token).get("role", String.class));
//    }
}
