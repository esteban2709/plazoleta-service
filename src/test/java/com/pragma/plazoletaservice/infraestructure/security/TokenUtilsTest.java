package com.pragma.plazoletaservice.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenUtilsTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWQiOjEsInJvbGVzIjpbIkFETUlOIl19.ABC123";
    private static final String ACCESS_TOKEN_SECRET = "access12token12secretAC12DSaa2s2dasd978";
    private HttpServletRequest httpServletRequest;
    private ServletRequestAttributes servletRequestAttributes;

    @BeforeEach
    void setUp() {
        httpServletRequest = mock(HttpServletRequest.class);
        servletRequestAttributes = mock(ServletRequestAttributes.class);

    }

    @Test
    void getUsernamePasswordAuthenticationToken_ValidToken_ReturnsAuthenticationToken() {
        // Arrange
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("user@example.com");
        when(claims.get("roles", List.class)).thenReturn(Collections.singletonList("ADMIN"));

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            io.jsonwebtoken.JwtParserBuilder jwtParserBuilder = mock(io.jsonwebtoken.JwtParserBuilder.class);
            io.jsonwebtoken.JwtParser jwtParser = mock(io.jsonwebtoken.JwtParser.class);
            io.jsonwebtoken.Jws<Claims> jws = mock(io.jsonwebtoken.Jws.class);

            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.setSigningKey(any(javax.crypto.SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(VALID_TOKEN)).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            // Act
            UsernamePasswordAuthenticationToken result = TokenUtils.getUsernamePasswordAuthenticationToken(VALID_TOKEN);

            // Assert
            assertNotNull(result);
            assertEquals("user@example.com", result.getPrincipal());
            assertTrue(result.getAuthorities().stream()
                    .anyMatch(auth -> auth.equals(new SimpleGrantedAuthority("ADMIN"))));
        }
    }

    @Test
    void getUsernamePasswordAuthenticationToken_InvalidToken_ReturnsNull() {
        // Arrange
        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class)) {
            io.jsonwebtoken.JwtParserBuilder jwtParserBuilder = mock(io.jsonwebtoken.JwtParserBuilder.class);
            io.jsonwebtoken.JwtParser jwtParser = mock(io.jsonwebtoken.JwtParser.class);

            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.setSigningKey(any(javax.crypto.SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));

            // Act
            UsernamePasswordAuthenticationToken result = TokenUtils.getUsernamePasswordAuthenticationToken("invalid.token");

            // Assert
            assertNull(result);
        }
    }

    @Test
    void getTokenFromRequest_ValidToken_ReturnsToken() {
        // Arrange
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(servletRequestAttributes);
            when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

            // Act
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);

            String result = TokenUtils.getTokenFromRequest();

            // Assert
            assertEquals(VALID_TOKEN, result);
        }
    }

    @Test
    void getTokenFromRequest_NoToken_ReturnsNull() {
        // Arrange
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(servletRequestAttributes);
            when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

            // Act
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);

            String result = TokenUtils.getTokenFromRequest();

            // Assert
            assertNull(result);
        }
    }

    @Test
    void getCompleteTokenFromRequest_ValidToken_ReturnsFullToken() {
        // Arrange
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(servletRequestAttributes);
            when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);

            // Act
            String result = TokenUtils.getCompleteTokenFromRequest();

            // Assert
            assertEquals("Bearer " + VALID_TOKEN, result);
        }
    }

    @Test
    void getUserIdFromToken_ValidToken_ReturnsUserId() {
        // Arrange
        Claims claims = mock(Claims.class);
        when(claims.get("id", Long.class)).thenReturn(1L);

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class);
             MockedStatic<TokenUtils> mockedTokenUtils = Mockito.mockStatic(TokenUtils.class, invocation -> {
                 if (invocation.getMethod().getName().equals("getTokenFromRequest")) {
                     return VALID_TOKEN;
                 }
                 return invocation.callRealMethod();
             })) {
            io.jsonwebtoken.JwtParserBuilder jwtParserBuilder = mock(io.jsonwebtoken.JwtParserBuilder.class);
            io.jsonwebtoken.JwtParser jwtParser = mock(io.jsonwebtoken.JwtParser.class);
            io.jsonwebtoken.Jws<Claims> jws = mock(io.jsonwebtoken.Jws.class);

            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.setSigningKey(any(javax.crypto.SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(VALID_TOKEN)).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            // Act
            Long result = TokenUtils.getUserIdFromToken();

            // Assert
            assertEquals(1L, result);
        }
    }

    @Test
    void getRoleIdFromToken_ValidToken_ReturnsFirstRole() {
        // Arrange
        Claims claims = mock(Claims.class);
        when(claims.get("roles", List.class)).thenReturn(Arrays.asList("ADMIN", "USER"));

        try (MockedStatic<Jwts> mockedJwts = Mockito.mockStatic(Jwts.class);
             MockedStatic<TokenUtils> mockedTokenUtils = Mockito.mockStatic(TokenUtils.class, invocation -> {
                 if (invocation.getMethod().getName().equals("getTokenFromRequest")) {
                     return VALID_TOKEN;
                 }
                 return invocation.callRealMethod();
             })) {
            io.jsonwebtoken.JwtParserBuilder jwtParserBuilder = mock(io.jsonwebtoken.JwtParserBuilder.class);
            io.jsonwebtoken.JwtParser jwtParser = mock(io.jsonwebtoken.JwtParser.class);
            io.jsonwebtoken.Jws<Claims> jws = mock(io.jsonwebtoken.Jws.class);

            mockedJwts.when(Jwts::parserBuilder).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.setSigningKey(any(javax.crypto.SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(VALID_TOKEN)).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            // Act
            String result = TokenUtils.getRoleIdFromToken();

            // Assert
            assertEquals("ADMIN", result);
        }
    }
}