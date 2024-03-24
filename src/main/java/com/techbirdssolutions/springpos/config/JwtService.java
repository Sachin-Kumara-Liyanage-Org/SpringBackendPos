package com.techbirdssolutions.springpos.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class provides services related to JSON Web Tokens (JWT).
 * It provides methods to extract username and expiration date from a token, validate a token, and generate a token.
 * It uses the HS256 algorithm for signing the tokens.
 */
@Component
public class JwtService {
    @Value("${jwt.secret}")
    public String SECRET;

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date extracted from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a claim from the given JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver a Function that takes a Claims object and returns a value of type T
     * @return the value of the claim extracted from the token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return a Claims object containing all claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks whether the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the given JWT token.
     * The token is valid if the username in the token matches the username of the given UserDetails object and the token is not expired.
     *
     * @param token the JWT token
     * @param userDetails the UserDetails object
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username
     * @return the generated JWT token
     */
    public String GenerateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }
    /**
     * Generates a refresh JWT token for the given username.
     *
     * @param username the username
     * @return the generated refresh JWT token
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, username);
    }
    /**
     * Creates a refresh JWT token for the given username.
     *
     * @param claims a Map containing the claims to be included in the token
     * @param username the username
     * @return the created refresh JWT token
     */
    private String createRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    /**
     * Creates a JWT token for the given username.
     *
     * @param claims a Map containing the claims to be included in the token
     * @param username the username
     * @return the created JWT token
     */
    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    /**
     * Returns the signing key for the JWT tokens.
     *
     * @return the signing key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}