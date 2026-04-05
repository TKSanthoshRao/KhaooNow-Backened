package com.food.khaaonow.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Base64 encoded (256-bit minimum for HS256)
    private static final String SECRET_KEY =
            "c3VwZXJzZWNyZXRrZXlmb3Jqd3RhdXRoZW50aWNhdGlvbg==";

    private static final long EXPIRATION_TIME = 1000L * 60 * 20; // 3 minutes

    /* ===================== TOKEN GENERATION ===================== */

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {

        return Jwts.builder()
                .claims(extraClaims)                  // custom claims only
                .subject(userDetails.getUsername())   // sub
                .issuedAt(new Date())                 // iat
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )                                     // exp
                .signWith(getSigningKey())             // algorithm inferred
                .compact();
    }

    /* ===================== TOKEN VALIDATION ===================== */

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /* ===================== CLAIM EXTRACTION ===================== */

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token,
                               Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /* ===================== SIGNING KEY ===================== */

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


































//package com.food.khaaonow.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    private static final String SECRET_KEY =
//            "f2b4c9a0d5e6a1b3c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8";
//
//    private static final Long EXPIRATION_TIME = 1000L * 60 * 3;
//
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(Map.of(),userDetails);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims,
//                                UserDetails userDetails) {
//
//        return Jwts.builder()
//                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date())
//                .expiration(
//                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
//                )
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String userName = extractUsername(token);
//        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token,Claims::getExpiration);
//    }
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims =extractAllClaims(token);
//        claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token).getBody();
//    }
//
//    private byte[] getSigningKey() {
//            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//            return Keys.hmacShaKeyFor(keyBytes).getEncoded();
//    }
//
//
//}
