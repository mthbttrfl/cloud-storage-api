package org.example.cloudstorageapi.service.storage.minio;

import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.mapper.StorageMapper;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.service.storage.StorageDirectoryService;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.provider.StorageDirectoryProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinIOStorageDirectoryService<I> implements StorageDirectoryService<RespResourceInfoDTO, I> {

    private final StorageDirectoryProvider<I, Item, PathDetails> provider;
    private final StorageMapper<RespResourceInfoDTO, Item> mapper;

    @Override
    public RespResourceInfoDTO create(I id, String path) {
        PathDetails details = provider.create(id, path);
        return mapper.mapObject(details.getFullPath(), 0L, true);
    }

    @Override
    public List<RespResourceInfoDTO> getObjects(I id, String path) {
        List<Item> objects = provider.getObjects(id, path);
        return mapper.mapObjects(objects, true);
    }
}
