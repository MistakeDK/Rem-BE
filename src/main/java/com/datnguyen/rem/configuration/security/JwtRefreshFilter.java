package com.datnguyen.rem.configuration.security;

import com.datnguyen.rem.dto.request.LogoutRequest;
import com.datnguyen.rem.service.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class JwtRefreshFilter extends OncePerRequestFilter {
    AuthenticationServiceImpl authenticationServiceImpl;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if (token != null) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                if (authenticationServiceImpl.needsRefresh(signedJWT)) {
                    String newToken = authenticationServiceImpl.refreshJwt(signedJWT);
                    response.setHeader("Authorization", "Bearer " + newToken);
                    authenticationServiceImpl.logout(LogoutRequest.builder().token(token).build());
                }
            } catch (ParseException | JOSEException e) {
                throw new ServletException("Invalid JWT token", e);
            }
        }
        filterChain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
