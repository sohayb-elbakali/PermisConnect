package com.autoecole.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl("https://permisconnecte.com");
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setName("PermisConnecte Support");
        contact.setEmail("support@permisconnecte.com");
        contact.setUrl("https://permisconnecte.com");

        License license = new License()
            .name("MIT License")
            .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
            .title("PermisConnecte API Documentation")
            .version("1.0.0")
            .description("This API exposes endpoints for managing driving school operations, including scheduling, user management, and courses.")
            .contact(contact)
            .license(license);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer, prodServer));
    }
}
