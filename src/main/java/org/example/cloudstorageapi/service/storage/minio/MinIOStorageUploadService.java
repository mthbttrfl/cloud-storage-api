package org.example.cloudstorageapi.service.storage.minio;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.exception.StorageUploadException;
import org.example.cloudstorageapi.service.storage.StorageResourceService;
import org.example.cloudstorageapi.service.storage.StorageUploadService;
import org.example.cloudstorageapi.storage.provider.StorageUploadProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.example.cloudstorageapi.constant.StorageErrorMessages.Upload.DAMAGED_FILE;

@Service
@RequiredArgsConstructor
public class MinIOStorageUploadService<I> implements StorageUploadService<RespResourceInfoDTO, I> {

    private final StorageUploadProvider<I> provider;
    private final StorageResourceService<RespResourceInfoDTO, I> resourceService;

    @Override
    public List<RespResourceInfoDTO> upload(I id, String path, List<MultipartFile> files){

        List<RespResourceInfoDTO> result = new ArrayList<>();

        try{
            for (MultipartFile file : files){
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
        }catch (IOException ex){
            throw new StorageUploadException(DAMAGED_FILE, ex);
        }
        return result;
    }
}
