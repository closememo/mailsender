package com.closememo.mailsender.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI apiOpenAPI() {
    return new OpenAPI()
        .servers(List.of(new Server().url("/")))
        .components(new Components()
            .addSecuritySchemes("System", new SecurityScheme()
                .name("X-SYSTEM-KEY")
                .type(Type.APIKEY)
                .in(In.HEADER)))
        .security(List.of(new SecurityRequirement().addList("System")));
  }
}
