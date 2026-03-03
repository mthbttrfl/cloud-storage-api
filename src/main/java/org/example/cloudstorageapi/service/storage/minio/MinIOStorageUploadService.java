package org.example.cloudstorageapi.service.storage.minio;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.service.storage.StorageResourceService;
import org.example.cloudstorageapi.service.storage.StorageUploadService;
import org.example.cloudstorageapi.storage.provider.StorageUploadProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinIOStorageUploadService<I> implements StorageUploadService<RespResourceInfoDTO, I> {

    private final StorageUploadProvider<I> provider;
    private final StorageResourceService<RespResourceInfoDTO, I> resourceService;

    @Override
    public List<RespResourceInfoDTO> upload(I id, String path, List<MultipartFile> files) throws IOException {

        List<RespResourceInfoDTO> result = new ArrayList<>();

        List<MultipartFile> sortedFiles = files.stream()
                .sorted()
                .toList();

        for (MultipartFile file : sortedFiles){
            provider.upload(id,
                    path,
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getInputStream()
            );

            RespResourceInfoDTO objectInfo = resourceService.getObjectInfo(id, path + file.getOriginalFilename());
            result.add(objectInfo);
        }

        return result;
    }
}
