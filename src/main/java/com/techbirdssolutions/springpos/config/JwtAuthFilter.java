package com.techbirdssolutions.springpos.config;

import com.techbirdssolutions.springpos.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * This class is a filter that intercepts each request once and performs JWT authentication.
 * It checks the Authorization header of the request for a JWT token.
 * If a token is found, it extracts the username from the token, loads the UserDetails object for the username, and validates the token.
 * If the token is valid, it creates a UsernamePasswordAuthenticationToken object with the UserDetails object and sets it in the SecurityContext.
 * The filter then passes the request and response to the next filter in the chain.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    CustomUserDetailsService userDetailsServiceImpl;

    /**
     * This method is called for each request to perform JWT authentication.
     * It checks the Authorization header of the request for a JWT token.
     * If a token is found, it extracts the username from the token, loads the UserDetails object for the username, and validates the token.
     * If the token is valid, it creates a UsernamePasswordAuthenticationToken object with the UserDetails object and sets it in the SecurityContext.
     * The method then passes the request and response to the next filter in the chain.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param filterChain the FilterChain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);
    }
}
