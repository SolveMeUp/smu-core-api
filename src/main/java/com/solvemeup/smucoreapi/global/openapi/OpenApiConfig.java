package com.solvemeup.smucoreapi.global.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI solveMeUpOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("SolveMeUp Core API")
                .description("코딩 문제 해결 플랫폼 SolveMeUp의 코어 API 문서")
                .version("v1"));
    }
}
