package com.datnguyen.rem.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PublicKey;
import java.util.Objects;

@Configuration
public class CloundinaryConfig {
    @Value("${cloundinary.cloundName}")
    String cloundName;
    @Value("${cloundinary.api_key}")
    String apiKey;
    @Value("${cloundinary.api_secret}")
    String apiSecret;
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",cloundName,
                "api_key",apiKey,
                "api_secret",apiSecret
        ));
    }
}
