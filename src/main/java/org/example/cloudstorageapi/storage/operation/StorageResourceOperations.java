package org.example.cloudstorageapi.storage.operation;

import org.example.cloudstorageapi.storage.PathDetails;

import java.util.List;

public interface StorageResourceOperations <R, I, P extends PathDetails>{

    R getObjectInfo(P detail);
    void removeObject(P detail);
    void removeObjects(P detail);
    List<I> searchObjects(P detail);
    void moveOrRenameObject(P from, P to);
    void moveOrRenameObjects(P from, P to);
}
