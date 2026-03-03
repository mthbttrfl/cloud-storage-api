package org.example.cloudstorageapi.storage.provider;

import org.example.cloudstorageapi.storage.PathDetails;

import java.util.List;

public interface StorageDirectoryProvider <P, R, D extends PathDetails>{

    D create(P rootFolder, String path);
    List<R> getObjects(P rootFolder, String path);
}
