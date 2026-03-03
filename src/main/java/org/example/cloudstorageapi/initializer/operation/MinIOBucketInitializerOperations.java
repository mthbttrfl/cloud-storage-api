package org.example.cloudstorageapi.initializer.operation;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.exception.StorageException;
import org.springframework.stereotype.Component;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.Bucket.FAIL_CHECK_EXIST_FORMATTED;
import static org.example.cloudstorageapi.constant.StorageErrorMessages.Bucket.FAIL_CREATE_FORMATTED;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinIOBucketInitializerOperations implements BucketInitializerOperations {

    private final MinioClient minioClient;

    @Override
    public void createBucketIfNotExists(String bucketName) {
        if(!bucketExists(bucketName)){
            makeBucket(bucketName);
            log.info("Bucket is created: {}", bucketName);
        }else{
            log.info("The bucket already exists: {}", bucketName);
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            log.debug("Check bucket exist: {}", bucketName);
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Fail check bucket exist, bucket name ({}), message: {}", bucketName, ex.getMessage(), ex);
            throw new StorageException(FAIL_CHECK_EXIST_FORMATTED.formatted(bucketName), ex);
        }
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            log.debug("Create bucket: {}", bucketName);
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Fail create bucket, bucket name ({}), message: {}", bucketName, ex.getMessage(), ex);
            throw new StorageException(FAIL_CREATE_FORMATTED.formatted(bucketName), ex);
        }
    }
}
