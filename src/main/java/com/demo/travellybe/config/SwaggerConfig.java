package com.demo.travellybe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Travelly API")
                        .description("Travelly API 문서입니다.")
                        .version("1.0.0"));
//                .components(new Components()
//                        .addSecuritySchemes("basicAuth",
//                                new io.swagger.v3.oas.models.security.SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("basic")));
    }
}