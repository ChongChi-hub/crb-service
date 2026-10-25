package com.crb.seaturtlewebservice.security;

import com.crb.seaturtlewebservice.service.RefreshLoginService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    @Value("${app.jwtSecret}")
    private String jwtSecretKey;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    private final RefreshLoginService refreshLoginService;

    @Autowired
    public TokenProvider(RefreshLoginService refreshLoginService) {
        this.refreshLoginService = refreshLoginService;
    }

    public String generateToken(Authentication authentication) {
        Date createdDate = new Date();
        Date expiryDate = new Date(createdDate.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public boolean validateToken(String jwtString, HttpServletRequest request) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(jwtString);
            return true;
        } catch (SignatureException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // When token expired but user have not logout yet,
            // Call refreshLogin to refresh token for this user
            refreshLoginService.refreshLogin(request);
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
}