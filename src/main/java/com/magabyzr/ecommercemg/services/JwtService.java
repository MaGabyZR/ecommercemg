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
/*    @Value("${spring.jwt.secret}")            //replaces with a Jwt Config object
    private String secret;*/

    //Inject our secret with a Jwt Config Object.
    private final JwtConfig jwtConfig;

    //Generate access tokens.
    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    //Generate refresh tokens.
    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))              //*1000 because you are dealing with milliseconds.
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    //Validate tokens.
    public boolean validateToken(String token){
        try{
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch(JwtException ex){
            return false;
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

    //Get the id from a token.
    public Long getUserIdFromToken(String token){
        return Long.valueOf(getClaims(token).getSubject());
    }
    //Get the role from the token
    public Role getRoleFromToken(String token){
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}
