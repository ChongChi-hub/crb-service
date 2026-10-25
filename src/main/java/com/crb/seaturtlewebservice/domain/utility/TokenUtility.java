package com.crb.seaturtlewebservice.domain.utility;

import com.crb.seaturtlewebservice.constant.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.Date;

public class TokenUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtility.class);

    public static String generateToken(Authentication authentication) {
        LOGGER.debug("TokenUtility -> generateToken invoked!");
        Date createdDate = new Date();
        Date expiryDate = new Date(createdDate.getTime() + TokenConstant.JWT_EXPIRATION_IN_MS);
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, TokenConstant.JWT_SECRET_KEY)
                .compact();
        return token;
    }

    public static String getUsernameFromJwt(String jwtString) {
        LOGGER.debug("TokenUtility -> getUsernameFromJwt invoked!");
        Claims claims = Jwts.parser()
                .setSigningKey(TokenConstant.JWT_SECRET_KEY)
                .parseClaimsJws(jwtString)
                .getBody();
        return claims.getSubject();
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        LOGGER.debug("TokenUtility -> getTokenFromRequest invoked!");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        }
        return null;
    }
}