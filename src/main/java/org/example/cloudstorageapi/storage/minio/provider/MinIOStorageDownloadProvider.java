package org.example.cloudstorageapi.storage.minio.provider;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageDownloadOperation;
import org.example.cloudstorageapi.storage.provider.StorageDownloadProvider;
import org.example.cloudstorageapi.storage.resolver.VirtualPathWithRootFolderResolver;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class MinIOStorageDownloadProvider <P> implements StorageDownloadProvider<P> {

    private final VirtualPathWithRootFolderResolver<P, PathDetails> resolver;
    private final StorageDownloadOperation<PathDetails> storage;

    @Override
    public void downloadOutStream(P rootFolder, String path, OutputStream outputStream) {
        PathDetails detail = resolver.resolve(rootFolder, path);
        if (detail.isDirectory()){
            storage.downloadZipStreamDirectory(detail, outputStream);
        }else{
            storage.downloadZipStreamFile(detail, outputStream);
        }
    }
}
