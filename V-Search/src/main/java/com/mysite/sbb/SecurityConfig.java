package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/withdraw")).authenticated() // /withdraw에 대한 접근 권한을 인증된 사용자로 설정
                .requestMatchers(new AntPathRequestMatcher("/uploadForm")).authenticated() // /uploadForm에 대한 접근 권한을 인증된 사용자로 설정
                .requestMatchers(new AntPathRequestMatcher("/Member/**")).authenticated() //userpage에 대한 접근 권한을 인증된 사용자로 설정
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 나머지 모든 요청은 모든 사용자에게 허용
            .csrf().disable()
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login")
                .defaultSuccessUrl("/"))
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManager() {
            @Override
            public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
                return null;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
