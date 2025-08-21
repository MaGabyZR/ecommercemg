package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }
    //Validate token
    public boolean isExpired(){
        return claims.getExpiration().before(new Date());
    }
    //Get the user id.
    public Long getUserId(){
        return Long.valueOf(claims.getSubject());
    }
    //Get the role from a token
    public Role getRole(){
        return Role.valueOf(claims.get("role", String.class));
    }
    //Converting a token to a String.
    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }

}
