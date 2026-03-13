package org.example.cloudstorageapi.storage.minio.operation;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.constant.MinIOErrorResponseCode;
import org.example.cloudstorageapi.exception.StorageConflictException;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.exception.StorageNotFoundException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.minio.MinIOPathDetails;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class MinIOBaseStorageOperations {

    protected final MinioClient minioClient;
    protected final MinIOBucketsNamesProperties buckets;

    protected final void assertObjectExists(PathDetails pathDetails) {

        log.debug("Start checking object exists: {}", pathDetails.getFullPath());

        if (!objectExists(pathDetails)) {
            throw new StorageNotFoundException(RESOURCE_NOT_EXIST_FORMATTED.formatted(pathDetails.getPathWithOutRootFolder()));
        }

        log.debug("Check object exists successfully: {}", pathDetails.getFullPath());
    }

    protected final void assertObjectNotExists(PathDetails pathDetails) {

        log.debug("Start checking object not exists: {}", pathDetails.getFullPath());

        if (objectExists(pathDetails)) {
            throw new StorageConflictException(RESOURCE_EXIST_FORMATTED.formatted(pathDetails.getPathWithOutRootFolder()));
        }

        log.debug("Check object not exists successfully: {}", pathDetails.getFullPath());
    }

    protected final void assertDirectoryExists(PathDetails pathDetails) {

        log.debug("Start checking directory exists: {}", pathDetails.getFullPath());

        if (directoryNotExists(pathDetails)) {
            throw new StorageNotFoundException(DIRECTORY_NOT_EXIST_FORMATTED.formatted(pathDetails.getPathWithOutRootFolder()));
        }

        log.debug("Check directory exists successfully: {}", pathDetails.getFullPath());
    }

    protected final void assertNameNotExists(PathDetails pathDetails){
        PathDetails pathDetailsName;
        if (pathDetails.isDirectory()){
            pathDetailsName = new MinIOPathDetails(pathDetails.getRootFolder(), pathDetails.getPrefix() + pathDetails.getLastSegmentName());
        }else{
            pathDetailsName = new MinIOPathDetails(pathDetails.getRootFolder(), pathDetails.getPathWithOutRootFolder() + "/");
        }
        try {
            assertObjectNotExists(pathDetailsName);
        }catch (StorageConflictException ex){
            throw new StorageConflictException(RESOURCE_NAME_EXIST_FORMATTED.formatted(pathDetails.getLastSegmentName()));
        }
    }

    protected final List<Item> listObjectsByPrefix(String prefix, boolean recursive){
        try{
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(buckets.getUser())
                            .prefix(prefix)
                            .recursive(recursive)
                            .build()
            );
            return convertList(results);
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    protected final void copyObject(PathDetails from, PathDetails to){
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(to.getFullPath())
                            .source(
                                    CopySource.builder()
                                            .bucket(buckets.getUser())
                                            .object(from.getFullPath())
                                            .build()
                            )
                            .build()
            );
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    protected final void ensureParentDirectories(PathDetails file) {
        String prefix = file.getRootFolder() + file.getPrefix();
        if (prefix.isBlank()) return;

        String[] parts = prefix.split("/");

        StringBuilder current = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) continue;

            current.append(part).append("/");

            PathDetails dir = new MinIOPathDetails(
                    file.getRootFolder(),
                    current.substring(file.getRootFolder().length())
            );

            if (directoryNotExists(dir)) {
                createDirectoryObject(dir);
            }
        }
    }

    protected final void createDirectoryObject(PathDetails dir) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(dir.getFullPath())
                            .stream(InputStream.nullInputStream(), 0, -1)
                            .build()
            );
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    private boolean objectExists(PathDetails pathDetails) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(pathDetails.getFullPath())
                            .build()
            );
            return true;

        } catch (ErrorResponseException ex) {
            if (MinIOErrorResponseCode.NO_SUCH_KEY.equalsIgnoreCase(ex.errorResponse().code())) {
                return false;
            }
            throw new StorageException(ex.getMessage(), ex);

        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    private boolean directoryNotExists(PathDetails pathDetails) {
        if(pathDetails.hasRootFolder() && pathDetails.getPathWithOutRootFolder().isEmpty()){
            return false;
        }

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(buckets.getUser())
                            .prefix(pathDetails.getFullPath())
                            .recursive(false)
                            .maxKeys(1)
                            .build()
            );

            return !results.iterator().hasNext();

        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    private List<Item> convertList(Iterable<Result<Item>> objects){
        List<Item> results = new ArrayList<>();
        try {
            for(Result<Item> object : objects){
                results.add(object.get());
            }
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }

        return results;
    }
}
