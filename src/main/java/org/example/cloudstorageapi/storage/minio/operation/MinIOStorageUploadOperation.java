package org.example.cloudstorageapi.storage.minio.operation;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageUploadOperation;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
public class MinIOStorageUploadOperation extends MinIOBaseStorageOperations implements StorageUploadOperation<PathDetails> {

    public MinIOStorageUploadOperation(MinioClient minioClient, MinIOBucketsNamesProperties buckets) {
        super(minioClient, buckets);
    }

    @Override
    public void upload(PathDetails detail, long size, String contentType, InputStream inputStream) {
        log.debug("Start upload object with size: {}, content type: {}, path: {}", size, contentType, detail.getFullPath());
        assertObjectNotExists(detail);
        assertNameNotExists(detail);
        ensureParentDirectories(detail);
        try{
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(buckets.getUser())
                            .object(detail.getFullPath())
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            log.debug("Uploading object successfully with size: {}, content type: {}, path: {}", size, contentType, detail.getFullPath());
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }
}
