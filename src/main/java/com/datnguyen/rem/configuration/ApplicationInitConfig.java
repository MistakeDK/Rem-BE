package com.datnguyen.rem.configuration;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import com.datnguyen.rem.repository.CategoryRepository;
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
    ApplicationRunner applicationRunner(UserRepository userRepository, CategoryRepository categoryRepository){
        return args -> {
            if(userRepository.findByusername("admin").isEmpty()){
                User user=new User();
                user.setUsername("admin");
                user.setRole(Role.ADMIN);
                user.setIsActive(true);
                user.setIsBan(false);
                user.setPassword(passwordEncoder.encode("12345678"));
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin,please change password");
            }
            if(!(categoryRepository.existsByName("Naruto")&& categoryRepository.existsByName("One Piece"))){
                Category categoryFirst=Category.builder().name("Naruto").build();
                categoryRepository.save(categoryFirst);
                Category categorySecond= Category.builder().name("One Piece").build();
                categoryRepository.save(categorySecond);
            }
        };
    }
}
