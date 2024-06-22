package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.*;
import com.datnguyen.rem.dto.response.AuthenticationResponse;
import com.datnguyen.rem.dto.response.IntrospectResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.UserMapper;
import com.datnguyen.rem.repository.httpClient.OutboundIdentityClient;
import com.datnguyen.rem.repository.UserRepository;
import com.datnguyen.rem.repository.httpClient.OutboundUserClient;
import com.datnguyen.rem.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.NonNull;
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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    BaseRedisServiceImpl baseRedisService;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    UserMapper userMapper;
    @NonFinal
    @Value("${jwt.private_key}")
    protected String PRIVATE_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${google.clientID}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${google.clientSecret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${google.redirectUri}")
    protected String REDIRECT_URI;

    @NonFinal
    @Value("${google.grantType}")
    protected String GRANT_TYPE;

    @Override
    @Transactional
    public AuthenticationResponse outboundAuthenticate(String code) throws JOSEException {
        ExchangeTokenRequest exchangeTokenRequest=ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build();
        var response=outboundIdentityClient.exchangeToken(exchangeTokenRequest);
        var userInfo=outboundUserClient.getUserInfo("json",response.getAccessToken());
        var user=userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(userMapper.toUserFromOutBound(userInfo)));
        String token=generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .username(user.getUsername())
                .build();
    }

    @Override
    public void logout(LogoutRequest request)
            throws JOSEException {
        try{
            var singedToken= verifyToken(request.getToken());
            String jit=singedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime=singedToken.getJWTClaimsSet().getExpirationTime();
            Duration duration=Duration.between(Instant.now(),expiryTime.toInstant());
            baseRedisService.Set(jit,Instant.now().toString(),duration.toSeconds());
        }catch (AppException | ParseException e){
            log.info("Token already expire");
        }
    }
    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        var singedToken= verifyToken(request.getRefreshToken());
        var username=singedToken.getJWTClaimsSet().getSubject();
        var user=userRepository.findByUsername(username).
                orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        var token=generateToken(user);
        return  AuthenticationResponse.builder()
                .authenticated(true)
                .username(user.getUsername())
                .token(token)
                .build();
    }
    @Override
    public String generateToken(User user) throws JOSEException {
        Date expiration=new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());
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
    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request)
            throws JOSEException {
        var user=userRepository.findByUsername(
                request.getUsername()).orElseThrow(()-> new  AppException(ErrorCode.USER_NOT_EXIST));
        if(!user.getIsActive()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated=
                passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token=generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .authenticated(true).build();
    }
    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token =request.getToken();
        boolean isValid=true;
        try{
        verifyToken(token);
        }catch (AppException e){
            isValid=false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
    @Override
    public String BuildScope(User user){
        StringJoiner stringJoiner=new StringJoiner("");
        if(!user.getRole().name().isEmpty())
           stringJoiner.add(user.getRole().name());
        return stringJoiner.toString();
    }
    @Override
    public SignedJWT verifyToken(String token)
            throws JOSEException, ParseException {
        JWSVerifier verifier=new MACVerifier(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8));
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expiryTime=signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified= signedJWT.verify(verifier);
        var user=userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject()).
                orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        if(user.getIsBan()){
            throw new AppException(ErrorCode.USER_IS_BAN);
        }
        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(baseRedisService.keyExists(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
    @Override
    public boolean needsRefresh(SignedJWT signedJWT)
            throws ParseException {
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        long timeToExpiry = expirationTime.getTime() - System.currentTimeMillis();
        // 30 minutes in milliseconds
        long REFRESH_THRESHOLD = 30 * 60 * 1000;
        return timeToExpiry < REFRESH_THRESHOLD;
    }

    public String refreshJwt(SignedJWT signedJWT)
            throws JOSEException, ParseException {
        String userId = signedJWT.getJWTClaimsSet().getStringClaim("userId");
        String username = signedJWT.getJWTClaimsSet().getSubject();

        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        if (user == null || !user.getUsername().equals(username)) {
            throw new RuntimeException("Invalid user information");
        }
        return generateToken(user);
    }
}
