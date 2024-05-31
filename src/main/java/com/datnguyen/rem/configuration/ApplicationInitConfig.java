package com.datnguyen.rem.configuration;

import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import com.datnguyen.rem.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByusername("admin").isEmpty()){
                User user= User.builder()
                        .username("admin")
                        .role(Role.ADMIN)
                        .isActive(true)
                        .password(passwordEncoder.encode("12345678"))
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin,please change password");
            }
        };
    }
}
