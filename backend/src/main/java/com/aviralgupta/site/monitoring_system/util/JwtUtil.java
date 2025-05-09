package com.aviralgupta.site.monitoring_system.util;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // TODO: CHANGE AND MOVE THE TOKEN TO ENV IN PROD
    private static final String SECRET_KEY = "4f49bbc3e5bce7b6483129523defd7911238aa3fee32c3cec65e15dfed7e88f65630acb16a6de92705aedbb9926dd09938f6f08f4d520b2e50b17ea62db73e3f88674b9838eeaefc0e74ea6e2b80b1256cadd5608cf6c71d4ba1220b58cba52faae1a00cb3ebc9d64b5b0978504098f8935bb570dccfd2172fbfb8b197933020d618b582ad662d48953f8b30362207e9470d3672aeb93db089936d92fa99dd9f7894df33d7f992df5c5ee9a00de77c23cdf0dd0dfe6af408ad2c7161da4b46a2aeba2d101e79609e1c24d950b3f532a2cddd8fa133564a58d462aa85546ba1c30ba766d0a5885db9b3ac5317d2e297d3f18290fb52467ca3af188217ec15abdb";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Or load from properties

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
