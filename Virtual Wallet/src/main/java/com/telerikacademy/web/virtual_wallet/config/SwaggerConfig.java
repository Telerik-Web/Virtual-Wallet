package com.telerikacademy.web.virtual_wallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Forum System")
                        .version("1.0")
                        .description("Forum System for everything automotive. With users who are able to create a post, " +
                                "update a post and delete a post. With some users being admins and thus allowing them to " +
                                "update other users posts and promote, block or delete other users."))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("authHeader", new SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")));
    }
}
