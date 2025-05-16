package ru.javaops.bootjava.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant-voting API")
                        .version("1.0")
                        .description("""
                                ## API General Description
                                
                                A voting system for deciding where to have lunch.
                                
                                2 types of users: admin and regular users
                                Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
                                Menu changes each day (admins do the updates)
                                Users can vote for a restaurant they want to have lunch at today
                                Only one vote counted per user
                                If user votes again the same day:
                                If it is before 11:00 we assume that he changed his mind.
                                If it is after 11:00 then it is too late, vote can't be changed
                                Each restaurant provides a new menu each day.
                                
                                """))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                        .components(new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }
}
