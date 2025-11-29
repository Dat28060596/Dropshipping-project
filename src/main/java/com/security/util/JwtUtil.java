package com.security.util;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

/**
 * JWT Utility class for token generation, validation, and extraction
 * Stateless authentication mechanism for React frontend
 */
public class JwtUtil {
    
    // JWT Secret - MUST be at least 32 characters for HS256
    private static final String SECRET_KEY = "your_very_long_random_secret_key_at_least_32_characters_here_for_hs256";
    
    // Token expiration: 1 hour in milliseconds
    private static final long EXPIRATION_TIME = 3600000; // 1 hour
    
    // Signing key
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    /**
     * Generate JWT token with user information and roles
     * @param username The username to include in token
     * @param roles The user's roles
     * @return JWT token string
     */
    public static String generateToken(String username, Collection<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", new ArrayList<>(roles));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate JWT token
     * @param token The JWT token to validate
     * @return true if token is valid and not expired
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token expired: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token unsupported: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("JWT token malformed: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("JWT signature validation failed: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract username from JWT token
     * @param token The JWT token
     * @return The username from the token
     */
    public static String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            System.out.println("Error extracting username: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract roles from JWT token
     * @param token The JWT token
     * @return List of roles from the token
     */
    @SuppressWarnings("unchecked")
    public static List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return (List<String>) claims.get("roles");
        } catch (JwtException e) {
            System.out.println("Error extracting roles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Extract all claims from JWT token
     * @param token The JWT token
     * @return Claims object containing all token claims
     */
    public static Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Error extracting claims: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if token has expired
     * @param token The JWT token
     * @return true if token is expired
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims != null && claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
