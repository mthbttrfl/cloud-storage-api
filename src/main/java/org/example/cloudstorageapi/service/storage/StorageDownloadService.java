package org.example.cloudstorageapi.service.storage;

import java.io.OutputStream;

public interface StorageDownloadService <I>{
    void download(I id, String path, OutputStream outputStream);
}
