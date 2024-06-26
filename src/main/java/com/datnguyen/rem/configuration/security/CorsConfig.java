package com.datnguyen.rem.configuration.security;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @NonFinal
    @Value("${dev.site}")
    String siteUrl;
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        CorsConfiguration config=new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(siteUrl);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**",config);
        config.addExposedHeader("Authorization");
        return  new CorsFilter(source);
    }
}
