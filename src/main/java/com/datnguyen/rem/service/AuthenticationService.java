package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.AuthenticationRequest;
import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.dto.request.LogoutRequest;
import com.datnguyen.rem.dto.request.RefreshRequest;
import com.datnguyen.rem.dto.response.AuthenticationResponse;
import com.datnguyen.rem.dto.response.IntrospectResponse;
import com.datnguyen.rem.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse outboundAuthenticate(String code) throws JOSEException;
    void logout(LogoutRequest request) throws JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    String generateToken(User user) throws JOSEException;
    AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException;
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    String BuildScope(User user);
    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
    boolean needsRefresh(SignedJWT signedJWT) throws ParseException;
    String refreshJwt(SignedJWT signedJWT) throws JOSEException, ParseException;
}
