package org.example.cloudstorageapi.storage.provider;

import java.io.InputStream;

public interface StorageUploadProvider<P> {

    void upload(P rootFolder, String path, String fileName, long size, String contentType, InputStream inputStream);
}
