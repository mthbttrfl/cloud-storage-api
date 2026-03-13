package org.example.cloudstorageapi.exception;

public class StorageUploadException extends StorageException {
    public StorageUploadException(String message) {
        super(message);
    }

    public StorageUploadException(String message, Throwable throwable) {
        super(message);
    }
}
