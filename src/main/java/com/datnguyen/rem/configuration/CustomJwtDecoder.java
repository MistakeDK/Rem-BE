package com.datnguyen.rem.configuration;

import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.private_key}")
    private String private_key;
    @Autowired
    private AuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder=null;

    @Override
    public Jwt decode(String token) throws JwtException {
       try {
           var response= authenticationService.introspect(IntrospectRequest.builder()
                   .token(token).build());//kiem tra token valid
           if(!response.isValid()){
               throw new JwtException("Token invalid");
           }
       }catch (JOSEException|ParseException e){
            throw new JwtException(e.getMessage());
       }
       if(Objects.isNull(nimbusJwtDecoder)){
           SecretKeySpec secretKeySpec=new SecretKeySpec(private_key.getBytes(),"HS512");
           nimbusJwtDecoder =nimbusJwtDecoder.withSecretKey(secretKeySpec)
                   .macAlgorithm(MacAlgorithm.HS512)
                   .build(); //set nimbusJwtDecoder de decode token
       }
       return nimbusJwtDecoder.decode(token);
    }
}
