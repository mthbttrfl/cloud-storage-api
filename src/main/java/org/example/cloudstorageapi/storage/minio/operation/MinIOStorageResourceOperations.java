package org.example.cloudstorageapi.storage.minio.operation;

import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.minio.MinIOPathDetails;
import org.example.cloudstorageapi.storage.operation.StorageResourceOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MinIOStorageResourceOperations extends MinIOBaseStorageOperations implements StorageResourceOperations<StatObjectResponse, Item,  PathDetails>{

    public MinIOStorageResourceOperations(MinioClient minioClient, MinIOBucketsNamesProperties buckets) {
        super(minioClient, buckets);
    }

    @Override
    public StatObjectResponse getObjectInfo(PathDetails detail) {
        log.debug("Start extract info: {}", detail.getFullPath());
        assertObjectExists(detail);
        try{
            StatObjectResponse statObjectResponse = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(detail.getFullPath())
                            .build()
            );
            log.debug("Extracting info successfully: {}", detail.getFullPath());
            return statObjectResponse;
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    @Override
    public void removeObject(PathDetails detail) {
        log.debug("Start remove object: {}", detail.getFullPath());
        assertObjectExists(detail);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(detail.getFullPath())
                            .build()
            );
            log.debug("Removing object successfully: {}", detail.getFullPath());
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    @Override
    public void removeObjects(PathDetails detail) {
        log.debug("Start remove objects: {}", detail.getFullPath());
        assertDirectoryExists(detail);
        try {
            List<MinIOPathDetails> details = listObjectsByPrefix(detail.getFullPath(), true).stream()
                    .map(item -> new MinIOPathDetails(item.objectName()))
                    .toList();
            for(PathDetails StorageDetail : details){
                removeObject(StorageDetail);
            }
            log.debug("Removing objects successfully: {}", detail.getFullPath());
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Item> searchObjects(PathDetails detail) {
        log.debug("Start searching object: {}", detail.getLastSegmentName());
        try{
            List<Item> items = listObjectsByPrefix(detail.getRootFolder(), true).stream()
                    .filter(item -> item.objectName().toLowerCase()
                            .contains(detail.getLastSegmentName().toLowerCase()))
                    .toList();
            log.debug("Searching objects successfully, count: {}", items.size());
            return items;
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    @Override
    public void moveOrRenameObject(PathDetails fromFile, PathDetails toFile){
        log.debug("Start moving or renaming object from {} to {}", fromFile.getFullPath(), toFile.getFullPath());
        assertObjectExists(fromFile);
        assertObjectNotExists(toFile);
        assertNameNotExists(toFile);

        copyObject(fromFile, toFile);
        removeObject(fromFile);
        log.debug("Moving or renaming object from {} to {}, successfully", fromFile.getFullPath(), toFile.getFullPath());
    }

    @Override
    public void moveOrRenameObjects(PathDetails fromDirectory, PathDetails toDirectory) {
        log.debug("Start moving or renaming objects from {} to {}", fromDirectory.getFullPath(), toDirectory.getFullPath());
        assertDirectoryExists(fromDirectory);
        assertObjectNotExists(toDirectory);
        assertNameNotExists(toDirectory);

        String fromPrefix = fromDirectory.getFullPath();

        List<Item> itemsFrom = listObjectsByPrefix(fromPrefix, true).stream()
                .toList();

        for (Item itemFrom : itemsFrom) {
            String fullObjectName = itemFrom.objectName();

            String relativePath = fullObjectName.substring(fromPrefix.length());

            PathDetails fromPath = new MinIOPathDetails(
                    fromDirectory.getRootFolder(),
                    fullObjectName.substring(fromDirectory.getRootFolder().length()));

            PathDetails toPath = new MinIOPathDetails(
                    toDirectory.getRootFolder(),
                    toDirectory.getPathWithOutRootFolder() + relativePath);

            copyObject(fromPath, toPath);
            removeObject(fromPath);
        }
        log.debug("Moving or renaming objects from {} to {}, successfully", fromDirectory.getFullPath(), toDirectory.getFullPath());
    }
}