package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio.buckets.names")
@Getter
@Setter
public class MinIOBucketsNamesProperties {

    private String user;
}
