package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.AuthenticationRequest;
import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.dto.request.LogoutRequest;
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
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
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

    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {
        var singToken=veryfyToken(request.getToken());
        String jit=singToken.getJWTClaimsSet().getJWTID();
        Date expiryTime=singToken.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken=InvalidToken.builder()
                .id(jit)
                .expireTime(expiryTime)
                .build();
        invalidTokenRepository.save(invalidToken);
    }
    private String generateToken(User user) throws JOSEException {
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder().
                subject(user.getUsername()).
                issueTime(new Date()).
                expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())).
                claim("scope",BuildScope(user)).
                jwtID(UUID.randomUUID().toString()).
                build();
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject=new JWSObject(header,payload);
        jwsObject.sign(new MACSigner(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8)));
        return jwsObject.serialize();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException {
        var user=userRepository.findByusername(
                request.getUsername()).orElseThrow(()-> new  AppException(ErrorCode.USER_NOT_EXIST));
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getPassword(),user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token=generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
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
        //StringJoiner stringJoiner=new StringJoiner("");
        StringJoiner stringJoiner=new StringJoiner(" ");
        //if(!CollectionUtils.isEmpty(user.getRole()))
        //   user.getRole().forEach(stringJoiner::add);

        if(!CollectionUtils.isEmpty(user.getRoles()))
           user.getRoles().forEach((role)->{
               stringJoiner.add("ROLE_"+role.getName());
               if(!CollectionUtils.isEmpty(role.getPermission())){
                   role.getPermission().forEach(permission -> {
                       stringJoiner.add(permission.getName());
                   });
               }});
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
