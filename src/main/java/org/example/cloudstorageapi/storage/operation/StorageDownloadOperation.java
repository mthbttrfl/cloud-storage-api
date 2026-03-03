package org.example.cloudstorageapi.storage.operation;

import org.example.cloudstorageapi.storage.PathDetails;

import java.io.OutputStream;

public interface StorageDownloadOperation <P extends PathDetails>{

    void downloadZipStreamDirectory(P detail, OutputStream outputStream);
    void downloadZipStreamFile(P detail, OutputStream outputStream);
}
