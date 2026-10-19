package com.paintfinder.global.config;


import com.paintfinder.global.handler.JwtTokenHandler;
import com.paintfinder.global.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenHandler jwtTokenHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     log.error("config2");
        http.authorizeRequests().antMatchers("/**").permitAll() // 모든 요청 인증 없이 접근 허용
                //.antMatchers("/users/**", "/settings/**").hasAuthority("Admin") // 특정 경로에 대해 Admin 권한 필요
                .anyRequest().authenticated(); // 그 외의 모든 요청에 대한 인증 필요

        // csfr 보호 비활성화
        http.csrf().disable();

        // 동일 출처로부터의 프레임만 허용
        http.headers().frameOptions().sameOrigin();
 
        return http.build();
    }
 
}
