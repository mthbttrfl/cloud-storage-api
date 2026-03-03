package org.example.cloudstorageapi.storage.provider;

import java.io.OutputStream;

public interface StorageDownloadProvider <P>{

    void downloadOutStream(P rootFolder, String path, OutputStream outputStream);
}
