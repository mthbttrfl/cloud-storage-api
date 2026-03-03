package org.example.cloudstorageapi.storage.operation;

import org.example.cloudstorageapi.storage.PathDetails;

import java.io.InputStream;

public interface StorageUploadOperation <P extends PathDetails>{

    void upload(P detail, long size, String contentType, InputStream inputStream);
}
