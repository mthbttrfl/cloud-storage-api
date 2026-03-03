package org.example.cloudstorageapi.mapper;

import io.minio.messages.Item;
import org.example.cloudstorageapi.dto.resp.RespResourceDirectoryInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceFileInfoDTO;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceInfoStorageMapper implements StorageMapper<RespResourceInfoDTO, Item> {

    @Override
    public RespResourceInfoDTO mapObject(String fullPath, long size, boolean hasRootFolder) {
        boolean isDirectory = fullPath.endsWith("/");

        String relativePath = hasRootFolder ? removeRootSegment(fullPath) : fullPath;

        if (relativePath.isEmpty()) {
            return new RespResourceDirectoryInfoDTO("", "/");
        }

        String pathWithoutTrailingSlash = isDirectory ? relativePath.substring(0, relativePath.length() - 1) : relativePath;

        String parent = "/";
        String name = pathWithoutTrailingSlash;

        int lastSlash = pathWithoutTrailingSlash.lastIndexOf('/');
        if (lastSlash != -1) {
            parent = pathWithoutTrailingSlash.substring(0, lastSlash + 1);
            name = pathWithoutTrailingSlash.substring(lastSlash + 1);
        }

        if (isDirectory) {
            return new RespResourceDirectoryInfoDTO(parent, name + "/");
        } else {
            return new RespResourceFileInfoDTO(parent, name, size);
        }
    }

    @Override
    public List<RespResourceInfoDTO> mapObjects(List<Item> items, boolean hasRootFolder) {
        List<RespResourceInfoDTO> result = new ArrayList<>();
        for (Item item : items) {
            result.add(mapObject(item.objectName(), item.size(), hasRootFolder));
        }
        return result;
    }

    private String removeRootSegment(String fullPath) {
        int firstSlash = fullPath.indexOf('/');
        if (firstSlash == -1) {
            return "";
        }
        return fullPath.substring(firstSlash + 1);
    }
}