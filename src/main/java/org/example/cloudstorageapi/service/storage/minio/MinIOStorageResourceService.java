package org.example.cloudstorageapi.service.storage.minio;

import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.mapper.StorageMapper;
import org.example.cloudstorageapi.service.storage.StorageResourceService;
import org.example.cloudstorageapi.storage.provider.StorageResourceProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinIOStorageResourceService<I> implements StorageResourceService<RespResourceInfoDTO, I> {

    private final StorageResourceProvider<StatObjectResponse, Item, I> provider;
    private final StorageMapper<RespResourceInfoDTO, Item> mapper;

    @Override
    public RespResourceInfoDTO getObjectInfo(I id, String path) {
        StatObjectResponse info = provider.getObjectInfo(id, path);
        return mapper.mapObject(info.object(), info.size(), true);
    }

    @Override
    public void remove(I id, String path) {
        provider.remove(id, path);
    }

    @Override
    public List<RespResourceInfoDTO> searchObjects(I id, String path) {
        List<Item> items = provider.searchObjects(id, path);
        return mapper.mapObjects(items, true);
    }

    @Override
    public RespResourceInfoDTO moveOrRename(I id, String pathFrom, String pathTo) {
        provider.moveOrRename(id, pathFrom, pathTo);
        return getObjectInfo(id, pathTo);
    }
}
