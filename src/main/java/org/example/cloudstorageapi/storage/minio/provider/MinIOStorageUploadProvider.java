package org.example.cloudstorageapi.storage.minio.provider;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.exception.StorageUploadException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageUploadOperation;
import org.example.cloudstorageapi.storage.provider.StorageUploadProvider;
import org.example.cloudstorageapi.storage.resolver.VirtualPathWithRootFolderResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.Upload.FILES_IN_FILE_FORMATTED;

@Component
@RequiredArgsConstructor
public class MinIOStorageUploadProvider<P> implements StorageUploadProvider<P> {

    private final VirtualPathWithRootFolderResolver<P, PathDetails> resolver;
    private final StorageUploadOperation<PathDetails> storage;

    @Override
    public void upload(P rootFolder, String path, String fileName, long size, String contentType, InputStream inputStream) {
        PathDetails parentDirectory = resolver.resolve(rootFolder, path);
        if(!parentDirectory.isDirectory()){
            throw new StorageUploadException(FILES_IN_FILE_FORMATTED.formatted(parentDirectory.getPathWithOutRootFolder()));
        }
        PathDetails detail = resolver.resolve(rootFolder, path + fileName);
        storage.upload(detail, size, contentType, inputStream);
    }
}
