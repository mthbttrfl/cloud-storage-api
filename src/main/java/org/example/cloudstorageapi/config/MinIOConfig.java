package org.example.cloudstorageapi.config;

import io.minio.MinioClient;
import org.example.cloudstorageapi.config.property.MinIOClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Bean
    public MinioClient minIoClient(MinIOClientProperties minIOClientProperties){
        return MinioClient.builder()
                .endpoint(minIOClientProperties.getUrl())
                .credentials(
                        minIOClientProperties.getAccessKey(),
                        minIOClientProperties.getSecretKey())
                .build();
    }
}
