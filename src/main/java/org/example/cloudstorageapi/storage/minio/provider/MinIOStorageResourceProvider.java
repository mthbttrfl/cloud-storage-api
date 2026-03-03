package org.example.cloudstorageapi.storage.minio.provider;

import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.model.OperationType;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageResourceOperations;
import org.example.cloudstorageapi.storage.provider.StorageResourceProvider;
import org.example.cloudstorageapi.storage.resolver.MoveOrRenameStrategyResolver;
import org.example.cloudstorageapi.storage.resolver.VirtualPathWithRootFolderResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MinIOStorageResourceProvider<P> implements StorageResourceProvider<StatObjectResponse, Item, P> {

    private final VirtualPathWithRootFolderResolver<P, PathDetails> resolver;
    private final StorageResourceOperations<StatObjectResponse, Item, PathDetails> storage;
    private final MoveOrRenameStrategyResolver<OperationType, PathDetails> strategyResolver;

    @Override
    public StatObjectResponse getObjectInfo(P rootFolder, String path) {
        PathDetails detail = resolver.resolve(rootFolder, path);
        return storage.getObjectInfo(detail);
    }

    @Override
    public void remove(P rootFolder, String path) {
        PathDetails detail = resolver.resolve(rootFolder, path);
        if(detail.isDirectory()){
            storage.removeObjects(detail);
        }else{
            storage.removeObject(detail);
        }
    }

    @Override
    public List<Item> searchObjects(P rootFolder, String query) {
        PathDetails detail = resolver.resolve(rootFolder, query);
        return storage.searchObjects(detail);
    }

    @Override
    public void moveOrRename(P rootFolder, String fromPath, String toPath) {

        PathDetails from = resolver.resolve(rootFolder, fromPath);
        PathDetails to   = resolver.resolve(rootFolder, toPath);

        OperationType operationType = strategyResolver.resolve(from, to);

        switch (operationType){
            case RENAME_FILE, MOVE_FILE -> storage.moveOrRenameObject(from, to);
            case RENAME_DIRECTORY, MOVE_DIRECTORY -> storage.moveOrRenameObjects(from, to);
        }
    }
}
