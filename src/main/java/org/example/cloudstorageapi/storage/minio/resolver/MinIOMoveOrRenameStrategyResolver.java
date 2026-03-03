package org.example.cloudstorageapi.storage.minio.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.exception.StorageMoveOrRenameException;
import org.example.cloudstorageapi.model.OperationType;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.resolver.MoveOrRenameStrategyResolver;
import org.springframework.stereotype.Component;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.MoveOrRename.*;

@Slf4j
@Component
public class MinIOMoveOrRenameStrategyResolver implements MoveOrRenameStrategyResolver<OperationType, PathDetails> {

    @Override
    public OperationType resolve(PathDetails from, PathDetails to) {
        log.debug("Start resolving strategy, from: {}, to: {}", from.getFullPath(), to.getFullPath());

        if (from.getFullPath().equals(to.getFullPath())) {
            throw new StorageMoveOrRenameException(PATH_NOT_DIFFERENT);
        }

        boolean samePrefix = from.getPrefix().equals(to.getPrefix());
        boolean sameName = from.getLastSegment().equals(to.getLastSegment());

        if(from.isDirectory()){
            if(!to.isDirectory()){
                throw new StorageMoveOrRenameException(DIRECTORY_TO_FILE);
            }
            if (to.getFullPath().startsWith(from.getFullPath())) {
                throw new StorageMoveOrRenameException(DIRECTORY_INTO_ITSELF_OR_SUBDIRECTORY);
            }
            if(samePrefix && !sameName){
                log.debug("Rename directory strategy for: (from){}, (to){}", from.getFullPath(), to.getFullPath());
                return OperationType.RENAME_DIRECTORY;
            } else if (!samePrefix && sameName) {
                log.debug("Move directory strategy for: (from){}, (to){}", from.getFullPath(), to.getFullPath());
                return OperationType.MOVE_DIRECTORY;
            }
        }else{
            if (to.isDirectory()){
                throw new StorageMoveOrRenameException(FILE_TO_DIRECTORY);
            }
            if(samePrefix && !sameName){
                log.debug("Rename file strategy for: (from){}, (to){}", from.getFullPath(), to.getFullPath());
                return OperationType.RENAME_FILE;
            } else if (!samePrefix && sameName) {
                log.debug("Move file strategy for: (from){}, (to){}", from.getFullPath(), to.getFullPath());
                return OperationType.MOVE_FILE;
            }
        }
        throw new StorageMoveOrRenameException(DIRECTORY_AND_FILE_CHANGED);
    }
}
