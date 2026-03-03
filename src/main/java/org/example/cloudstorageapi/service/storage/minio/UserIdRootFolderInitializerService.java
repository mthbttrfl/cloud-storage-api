package org.example.cloudstorageapi.service.storage.minio;

import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.service.storage.RootFolderInitializerService;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.provider.StorageDirectoryProvider;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdRootFolderInitializerService<I> implements RootFolderInitializerService<I> {

    private final StorageDirectoryProvider<I, Item, PathDetails> storage;

    @Override
    public void init(I parameter) {
        log.debug("Create root folder for user id: {}", parameter);

        storage.create(parameter, "");

        log.debug("Root folder creating success for user id: {}", parameter);
    }
}
