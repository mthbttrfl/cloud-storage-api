package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "cors.allowed")
@Component
@Getter
@Setter
public class CorsProperties {

    private List<String> origins;
    private List<String> methods;
    private boolean credentials;
}
