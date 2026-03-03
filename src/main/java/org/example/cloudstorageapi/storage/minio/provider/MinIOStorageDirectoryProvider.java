package org.example.cloudstorageapi.storage.minio.provider;

import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.exception.StorageDirectoryCreateException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageDirectoryOperations;
import org.example.cloudstorageapi.storage.provider.StorageDirectoryProvider;
import org.example.cloudstorageapi.storage.resolver.VirtualPathWithRootFolderResolver;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.FILE_NOT_DIRECTORY_FORMATTED;

@Component
@RequiredArgsConstructor
public class MinIOStorageDirectoryProvider<P> implements StorageDirectoryProvider<P, Item, PathDetails> {

    private final VirtualPathWithRootFolderResolver<P, PathDetails> resolver;
    private final StorageDirectoryOperations<Item, PathDetails> directory;

    @Override
    public PathDetails create(P rootFolder, String path) {
        PathDetails detail = resolver.resolve(rootFolder, path);
        if(!detail.isDirectory()){
            throw new StorageDirectoryCreateException(FILE_NOT_DIRECTORY_FORMATTED.formatted(detail.getPathWithOutRootFolder()));
        }
        directory.create(detail);
        return detail;
    }

    @Override
    public List<Item> getObjects(P rootFolder, String path) {
        PathDetails detail = resolver.resolve(rootFolder, path);
        return directory.getObjects(detail);
    }
}
