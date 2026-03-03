package org.example.cloudstorageapi.storage.minio.operation;

import io.minio.MinioClient;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.minio.MinIOPathDetails;
import org.example.cloudstorageapi.storage.operation.StorageDirectoryOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MinIOStorageDirectoryOperations extends MinIOBaseStorageOperations implements StorageDirectoryOperations<Item, PathDetails> {

    public MinIOStorageDirectoryOperations(MinioClient minioClient, MinIOBucketsNamesProperties buckets) {
        super(minioClient, buckets);
    }

    @Override
    public void create(PathDetails detail) {
        log.debug("Start creating directory: {}", detail.getFullPath());
        assertObjectNotExists(detail);
        assertNameNotExists(detail);
        PathDetails parent = new MinIOPathDetails(detail.getRootFolder(), detail.getPrefix());
        assertDirectoryExists(parent);

        createDirectoryObject(detail);
        log.debug("Creating directory successfully: {}", detail.getFullPath());
    }

    @Override
    public List<Item> getObjects(PathDetails details) {
        log.debug("Start extract objects: {}", details.getFullPath());
        assertDirectoryExists(details);
        try{
            List<Item> items = listObjectsByPrefix(details.getFullPath(), false).stream()
                    .filter(item -> !item.objectName().equals(details.getFullPath()))
                    .toList();
            log.debug("Extracting objects successfully: {}, count: {}", details.getFullPath(), items.size());
            return items;
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }
}
