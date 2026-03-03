package org.example.cloudstorageapi.service.storage;

import java.util.List;

public interface StorageDirectoryService <R, I>{

    R create(I id, String path);
    List<R> getObjects(I id, String path);
}
