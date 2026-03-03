package org.example.cloudstorageapi.initializer.operation;

import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface BucketInitializerOperations {

    void createBucketIfNotExists(String bucketName);

    boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    void makeBucket(String bucketName);
}
