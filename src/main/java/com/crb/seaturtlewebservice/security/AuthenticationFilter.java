package com.crb.seaturtlewebservice.security;

import com.crb.seaturtlewebservice.domain.utility.TokenUtility;
import com.crb.seaturtlewebservice.service.UserPrincipalService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final TokenProvider tokenProvider;
    private final UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String path = request.getServletPath();

        if (path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = TokenUtility.getTokenFromRequest(request);
            if (StringUtils.hasText(token)) {
                LOGGER.info("Found token (len={})", token.length());
            } else {
                LOGGER.info("No token found in request");
            }
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token, request)) {
                String username = TokenUtility.getUsernameFromJwt(token);
                UserDetails userDetails = userPrincipalService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            // Do not prevent request - just log if the token is invalid
            LOGGER.warn("JWT token is invalid or expired: {}", e.getMessage());
        }

        // Allow all requests to continue, even if not authenticated
        filterChain.doFilter(request, response);
    }
}
