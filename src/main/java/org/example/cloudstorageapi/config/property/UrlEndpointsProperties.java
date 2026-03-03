package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url.endpoints")
@Component
@Getter
@Setter
public class UrlEndpointsProperties {

    private String[] publics;
    private String logout;
}
