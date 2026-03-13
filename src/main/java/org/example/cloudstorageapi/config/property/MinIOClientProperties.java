package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio.client")
@Getter
@Setter
public class MinIOClientProperties {

    private String url;
    private String accessKey;
    private String secretKey;
}
