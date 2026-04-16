package com.skillpulse.backend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FirebaseSecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        System.out.println("Processing request: " + requestURI);
        
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header present: " + (authorizationHeader != null));

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
            return;
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        System.out.println("Token length: " + token.length());
        
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            
            System.out.println("Token verified successfully for user: " + email + " (UID: " + uid + ")");
            
            request.setAttribute("uid", uid);
            
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("Firebase token verification failed: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase token: " + e.getMessage());
        }
    }
}