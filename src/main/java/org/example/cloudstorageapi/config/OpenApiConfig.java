package org.example.cloudstorageapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CLOUD_STORAGE_API_DOCUMENTATION")
                        .version("v1")
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local server")
                ));
    }

    @Bean
    public OperationCustomizer cookieSessionCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(new CookieParameter()
                    .name("SESSION")
                    .description("Session cookie для авторизации")
                    .required(false)
            );
            return operation;
        };
    }
}
