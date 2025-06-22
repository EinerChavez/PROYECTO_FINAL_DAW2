package com.DW2.InnovaMedic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.DW2.InnovaMedic.util.Token;

import java.io.IOException;

@Component
public class JWTAuthotizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if( bearerToken != null && bearerToken.startsWith("Bearer ") ) {
            String token = bearerToken.replace("Bearer ", "");

            UsernamePasswordAuthenticationToken userPat = Token.getauth(token);
            SecurityContextHolder.getContext().setAuthentication(userPat);
        }

        filterChain.doFilter(request, response);
    }
}
