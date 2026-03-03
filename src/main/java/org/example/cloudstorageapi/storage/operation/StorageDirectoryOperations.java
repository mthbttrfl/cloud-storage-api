package org.example.cloudstorageapi.storage.operation;

import org.example.cloudstorageapi.storage.PathDetails;

import java.util.List;

public interface StorageDirectoryOperations <R, P extends PathDetails>{
    void create(P detail);
    List<R> getObjects(P details);
}
