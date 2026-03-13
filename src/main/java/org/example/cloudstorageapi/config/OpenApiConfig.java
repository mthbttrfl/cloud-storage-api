package org.example.cloudstorageapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.example.cloudstorageapi.config.property.DocumentationProperties;
import org.example.cloudstorageapi.config.property.SessionProperties;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(DocumentationProperties documentationProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title(documentationProperties.getTitle())
                        .version(documentationProperties.getVersion())
                )
                .servers(List.of(
                        new Server()
                                .url(documentationProperties.getServer())
                                .description(documentationProperties.getDescription())
                ));
    }

    @Bean
    public OperationCustomizer cookieSessionCustomizer(SessionProperties sessionProperties) {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(new CookieParameter()
                    .name(sessionProperties.getName())
                    .description("Session cookie для авторизации")
                    .required(false)
            );
            return operation;
        };
    }
}
