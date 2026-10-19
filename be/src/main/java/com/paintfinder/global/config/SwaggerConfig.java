package com.paintfinder.global.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    /**
     * ApiKey 객체를 생성하여 Authorization 헤더를 통해 JWT를 전달
     * @return Security Scheme 이름, HTTP header 이름, 헤더 타입
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    /**
     * SecurityContext 객체를 생성하여 보안 컨텍스트를 설정
     * securityReferences 메서드에 기본 인증 설정을 지정
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * SecurityReference 객체를 생성해 보안 레퍼런스 설정
     * AuthorizationScope를 통해 전체 접근 권한 부여
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    /**
     * Actuator 엔드포인트의 매핑 관리. Actuator의 웹 엔드포인트들이 어떤 URL 경로에 매핑될지를 결정한다.
     * Spring Boot Actuator: 애플리케이션의 상태 모니터링, 진단, 메트릭 수집 등을 위한 여러 가지 엔드포인트를 제공
     * @param webEndpointsSupplier  모든 웹 엔드포인트를 제공하는 공급자
     * @param servletEndpointsSupplier 서블릿 기반 엔드포인트를 제공하는 공급자
     * @param controllerEndpointsSupplier  Spring MVC 컨트롤러 기반 엔드포인트를 제공하는 공급자
     * @param endpointMediaTypes 엔드포인트가 지원하는 미디어 타입을 정의
     * @param corsProperties CORS(Cross-Origin Resource Sharing) 설정을 관리
     * @param webEndpointProperties Actuator의 웹 엔드포인트 설정을 관리
     * @param environment 애플리케이션의 환경 정보를 제공하는 인터페이스
     * @return WebMvcEndpointHandlerMapping Actuator - 엔드포인트를 관리
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    /**
     * Actuator 엔드포인트 링크를 매핑할지 여부를 결정
     * Actuator 엔드포인트 디스커버리가 활성화되었는지 확인, basePath가 비어 있지 않은지(StringUtils.hasText(basePath)) 또는 관리 포트 타입이 DIFFERENT인지 확인
     * @param webEndpointProperties 웹 엔드포인트와 관련된 설정
     * @param environment 애플리케이션의 환경 정보
     * @param basePath 엔드포인트의 기본 경로
     * @return 링크 매핑 등록 true/false
     */
    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
