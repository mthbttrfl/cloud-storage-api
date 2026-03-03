package org.example.cloudstorageapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio.root-folder.pattern")
@Component
@Getter
@Setter
public class MinIORootFolderProperties {

    private String prefix;
    private String suffix;
}
