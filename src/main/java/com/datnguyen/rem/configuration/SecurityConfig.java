package com.datnguyen.rem.configuration;

import com.datnguyen.rem.enums.Role;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final HttpMethod[] PUBLIC_METHOD={
            HttpMethod.GET,HttpMethod.PATCH,HttpMethod.POST,HttpMethod.PUT
    };
    private final String[] PUBLIC_ENDPOINTS={
            "/users/**","/auth/**",
            "/products/**",
            "/category/**",
            "/carts/**"
    };
    @Value("${dev.site}")
    private String url;
    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Value("${jwt.private_key}")
    private String secretKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests
                (request -> request.requestMatchers(PUBLIC_ENDPOINTS).
                permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
                .loginPage("/oauth2/authorization/github")
                .defaultSuccessUrl("/auth/LogInWithGithub")
                .failureUrl("/loginFailure"));
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())).
                authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return  jwtAuthenticationConverter;
    }

    @Bean
    OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService(){
        DefaultOAuth2UserService delegate=new DefaultOAuth2UserService();
        return request -> {
            OAuth2User oAuth2User=delegate.loadUser(request);
            String email=(String) oAuth2User.getAttributes().get("email");
            if(email==null){
                String token=request.getAccessToken().getTokenValue();
                RestTemplate restTemplate=new RestTemplate();
                HttpHeaders headers=new HttpHeaders();
                headers.add("Authorization","Bearer "+token);
                HttpEntity<String> entity=new HttpEntity<>("",headers);
                ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                        "https://api.github.com/user/emails", HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {});
                List<Map<String, Object>> emails = response.getBody();
                for (Map<String, Object> e : emails) {
                    if (Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified"))) {
                        email = (String) e.get("email");
                        break;
                    }
                }
            }
            Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
            attributes.put("email", email);

            return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "id");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder(10);
    }
}
