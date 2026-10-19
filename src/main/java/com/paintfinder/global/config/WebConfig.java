package com.paintfinder.global.config;

import com.paintfinder.global.handler.JwtTokenHandler;
import com.paintfinder.global.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private JwtTokenHandler jwtTokenHandler;

    public WebConfig(JwtTokenHandler jwtTokenHandler){
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtTokenHandler))
                .addPathPatterns(
                        "/auth/**"
                );
    }

//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry){
//
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000") // 클라이언트 도메인 명시
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*") // 허용할 헤더 지정
//                .allowCredentials(true) // 인증 정보 허용
//                .maxAge(3000); // preflight 요청 캐시 시간 설정
//
//    }
}
