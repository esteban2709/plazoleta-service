package com.pragma.plazoletaservice.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "access12token12secretAC12DSaa2s2dasd978";

    public static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            // Extract roles from token claims
            List<String> roles = claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (JwtException e) {
            return null;
        }
    }

    public static String getTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String token = attributes.getRequest().getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7); // Remove "Bearer " prefix
            }
        }
        return null;
    }

    public static String getCompleteTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getHeader("Authorization");
        }
        return null;
    }

    public static Long getUserIdFromToken() {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(getTokenFromRequest())
                    .getBody();
            return claims.get("id", Long.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public static String getRoleIdFromToken() {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(getTokenFromRequest())
                    .getBody();
            return claims.get("roles", List.class).get(0).toString();
        } catch (JwtException e) {
            return null;
        }
    }
}