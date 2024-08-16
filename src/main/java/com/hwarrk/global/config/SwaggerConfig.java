package com.hwarrk.global.config;

import com.hwarrk.global.common.constant.TokenConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(AUTHORIZATION, new SecurityScheme()
                                .name(AUTHORIZATION)
                                .type(HTTP)
                                .scheme(TokenConstant.BEARER_TYPE)
                                .bearerFormat(TokenConstant.JWT)))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Project of hwarrk")
                .description("API Swagger UI")
                .version("1.0.0");
    }
}
