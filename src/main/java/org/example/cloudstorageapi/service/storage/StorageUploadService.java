package org.example.cloudstorageapi.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageUploadService <R, I>{

    List<R> upload(I id, String path, List<MultipartFile> files) throws IOException;
}
