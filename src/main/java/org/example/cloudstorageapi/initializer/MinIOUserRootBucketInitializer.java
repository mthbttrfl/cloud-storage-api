package org.example.cloudstorageapi.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.initializer.operation.BucketInitializerOperations;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.Bucket.FAIL_INITIALIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinIOUserRootBucketInitializer implements Initializer {

    private final BucketInitializerOperations bucketInitializerOperations;
    private final MinIOBucketsNamesProperties minIOBucketsNamesProperties;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            log.info("Start bucket init: {}", minIOBucketsNamesProperties.getUser());
            bucketInitializerOperations.createBucketIfNotExists(minIOBucketsNamesProperties.getUser());
            log.info("Bucket successfully init: {}", minIOBucketsNamesProperties.getUser());
        }catch (Exception ex) {
            log.error("Fail init bucket, bucket name ({}), message: {}", minIOBucketsNamesProperties.getUser(), ex.getMessage(), ex);
            throw new StorageException(FAIL_INITIALIZE, ex);
        }
    }
}
