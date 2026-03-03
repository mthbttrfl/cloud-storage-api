package org.example.cloudstorageapi.service.storage.minio;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.service.storage.StorageDownloadService;
import org.example.cloudstorageapi.storage.provider.StorageDownloadProvider;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class MinIOStorageDownloadService <I> implements StorageDownloadService<I> {

    private final StorageDownloadProvider<I> provider;

    @Override
    public void download(I id, String path, OutputStream outputStream) {
        provider.downloadOutStream(id, path, outputStream);
    }
}
