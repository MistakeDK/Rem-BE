package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.AuthenticationRequest;
import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.dto.request.LogoutRequest;
import com.datnguyen.rem.dto.request.RefreshRequest;
import com.datnguyen.rem.dto.response.AuthenticationResponse;
import com.datnguyen.rem.dto.response.IntrospectResponse;
import com.datnguyen.rem.entity.InvalidToken;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.repository.InvalidTokenRepository;
import com.datnguyen.rem.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;
    @NonFinal
    @Value("${jwt.private_key}")
    protected String PRIVATE_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;
    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {
        try{
            var singedToken=veryfyToken(request.getToken());
            String jit=singedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime=singedToken.getJWTClaimsSet().getExpirationTime();
            InvalidToken invalidToken=InvalidToken.builder()
                    .id(jit)
                    .expireTime(expiryTime)
                    .build();
            invalidTokenRepository.save(invalidToken);
        }catch (AppException e){
            log.info("Token already expire");
        }
    }
    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        var singedToken=veryfyToken(request.getRefreshToken());
        String jit=singedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime=singedToken.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken=InvalidToken.builder()
                .id(jit)
                .expireTime(expiryTime)
                .build();
        invalidTokenRepository.save(invalidToken);
        var username=singedToken.getJWTClaimsSet().getSubject();
        var user=userRepository.findByusername(username).
                orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        var token=generateToken(user,false);
        return  AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }


    private String generateToken(User user,boolean isUseForRefreshToken) throws JOSEException {
        Date expiration=isUseForRefreshToken ?
                new Date(Instant.now().plus(REFRESHABLE_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                :new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());

        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder().
                subject(user.getUsername()).
                issueTime(new Date()).
                expirationTime(expiration).
                claim("scope",BuildScope(user)).
                jwtID(UUID.randomUUID().toString()).
                claim("userId",user.getId()).
                build();
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject=new JWSObject(header,payload);
        jwsObject.sign(new MACSigner(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8)));
        return jwsObject.serialize();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException {
        var user=userRepository.findByusername(
                request.getUsername()).orElseThrow(()-> new  AppException(ErrorCode.USER_NOT_EXIST));
        if(!user.getIsActive()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated= user.getPassword()==null ||
                passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token=generateToken(user,false);
        var refreshToken=generateToken(user,true);
        return AuthenticationResponse.builder()
                .token(token)
                .RefreshToken(refreshToken)
                .authenticated(true).build();
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token =request.getToken();
        boolean isValid=true;
        try{
        veryfyToken(token);
        }catch (AppException e){
            isValid=false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private String BuildScope(User user){
        StringJoiner stringJoiner=new StringJoiner("");
        if(!user.getRole().name().isEmpty())
           stringJoiner.add(user.getRole().name());
        return stringJoiner.toString();
    }
    private SignedJWT veryfyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier=new MACVerifier(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8));
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expiryTime=signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified= signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
