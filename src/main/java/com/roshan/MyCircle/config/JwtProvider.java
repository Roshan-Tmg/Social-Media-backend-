package com.roshan.MyCircle.config;


import com.roshan.MyCircle.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
//@Service
//public class JwtProvider {
////    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//private SecretKey key;   // Make it private and initialize in @PostConstruct
//
//    @PostConstruct
//    public void init() {
//        // Initialize key after bean construction
//        this.key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    }
//
//
//    public String generateToken(Authentication auth){
//        String jwt = Jwts.builder()
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime()+86400000))
//                .claim("email",auth.getName())
//                .signWith(key)
//                .compact();
//
//        return jwt;
//
//    }
//    public String getEmailFromToken(String jwt){
//        jwt = jwt.substring(7);
//        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
//
//        String email = String.valueOf(claims.get("email"));
//        return email;
//    }
//}

@Service
public class JwtProvider {

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT using your own User model
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())             // use email as subject
                .claim("role", user.getRole())           // store role (USER or ADMIN)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
                .signWith(key)
                .compact();
    }

    // Get email from token
    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7); // remove "Bearer "
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }

    // Get role from token
    public String getRoleFromToken(String jwt) {
        jwt = jwt.substring(7); // remove "Bearer "
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return claims.get("role", String.class);
    }
}

