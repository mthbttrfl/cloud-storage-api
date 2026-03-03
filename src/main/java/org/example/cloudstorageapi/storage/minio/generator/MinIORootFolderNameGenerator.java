package org.example.cloudstorageapi.storage.minio.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIORootFolderProperties;
import org.example.cloudstorageapi.storage.generator.RootFolderNameGenerator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinIORootFolderNameGenerator<P> implements RootFolderNameGenerator<P> {

    private final MinIORootFolderProperties minIORootFolderProperties;

    @Override
    public String generate(P parameter) {
        log.debug("Start generate root folder name for id: {}", parameter);

        String formatted = formattedPattern().formatted(parameter);

        log.debug("Root folder name generated: {}, id: {}", formatted, parameter);

        return formatted;
    }

    private String formattedPattern(){
        return minIORootFolderProperties.getPrefix() + "%s" + minIORootFolderProperties.getSuffix();
    }
}
