package org.example.cloudstorageapi.storage.resolver;

import org.example.cloudstorageapi.storage.PathDetails;

public interface MoveOrRenameStrategyResolver <T, P extends PathDetails>{

    T resolve(P fromPath, P to);
}
