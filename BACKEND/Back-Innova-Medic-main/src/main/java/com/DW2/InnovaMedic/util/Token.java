package com.DW2.InnovaMedic.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class Token {

    private final static String TOKEN_SECRETO = "QhwOQohWEdy6hrtEVpCR8IMJFgkSl57g";
    private final static Long TOKEN_DURACION = 3_600L;

    public static String crearToken(String user, String email, String rol){
        long expiracionTiempo = TOKEN_DURACION * 1_000L;
        Date expiracionFecha = new Date(System.currentTimeMillis() + expiracionTiempo);

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user);
        map.put("rol", rol);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expiracionFecha)
                .addClaims(map)
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getauth(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String rol = (String) claims.get("rol");
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

            return new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

        } catch (Exception e) {
            System.out.println("Error en el metodo {UsernamePasswordAuthenticationToken(): } " + e.getMessage());
            return null;
        }

    }

}
