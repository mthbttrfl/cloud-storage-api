package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "documentation")
@Getter
@Setter
public class DocumentationProperties {

    private String title;
    private String version;
    private String server;
    private String description;
}
