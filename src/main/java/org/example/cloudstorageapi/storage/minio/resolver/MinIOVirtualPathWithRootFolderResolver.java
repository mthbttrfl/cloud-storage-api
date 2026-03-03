package org.example.cloudstorageapi.storage.minio.resolver;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.generator.RootFolderNameGenerator;
import org.example.cloudstorageapi.storage.resolver.VirtualPathResolver;
import org.example.cloudstorageapi.storage.resolver.VirtualPathWithRootFolderResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinIOVirtualPathWithRootFolderResolver<P> implements VirtualPathWithRootFolderResolver<P, PathDetails> {

    private final RootFolderNameGenerator<P> rootFolderGenerator;
    private final VirtualPathResolver<PathDetails> virtualPathResolver;

    @Override
    public PathDetails resolve(P parameter, String userPath) {
        String rootFolder = rootFolderGenerator.generate(parameter);
        String combinedPath = rootFolder + "/" + userPath;
        return virtualPathResolver.resolve(combinedPath, true);
    }
}