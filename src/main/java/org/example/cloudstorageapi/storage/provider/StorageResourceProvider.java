package org.example.cloudstorageapi.storage.provider;

import java.util.List;

public interface StorageResourceProvider <R, I, P>{

    R getObjectInfo(P rootFolder, String path);
    void remove (P rootFolder, String path);
    List<I> searchObjects(P rootFolder, String query);
    void moveOrRename(P rootFolder, String pathFrom, String pathTo);
}
