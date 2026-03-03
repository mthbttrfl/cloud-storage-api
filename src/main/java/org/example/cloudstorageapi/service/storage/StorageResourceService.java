package org.example.cloudstorageapi.service.storage;

import java.util.List;

public interface StorageResourceService <R, I>{

    R getObjectInfo(I id, String path);
    void remove(I id, String path);
    List<R> searchObjects(I id, String path);
    R moveOrRename(I id, String pathFrom, String pathTo);
}
