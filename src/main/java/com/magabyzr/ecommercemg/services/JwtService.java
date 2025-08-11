package com.magabyzr.ecommercemg.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    //Generate tokens.
    public String generateToken(String email) {
        final long tokenExpiration = 86400;                                                         //number of seconds in a day, to make token valid for one day.

        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))              //*1000 because you are dealing with milliseconds.
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();                                                                             //to generate the token.
    }
    //Validate tokens.
    public boolean validateToken(String token){
        try{
            var claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().after(new Date());
        }
        catch(JwtException ex){
            return false;
        }
    }
}
