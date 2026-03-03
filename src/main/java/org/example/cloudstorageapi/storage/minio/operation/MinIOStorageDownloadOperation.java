package org.example.cloudstorageapi.storage.minio.operation;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.config.property.MinIOBucketsNamesProperties;
import org.example.cloudstorageapi.exception.StorageException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.operation.StorageDownloadOperation;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class MinIOStorageDownloadOperation extends MinIOBaseStorageOperations implements StorageDownloadOperation<PathDetails> {

    public MinIOStorageDownloadOperation(MinioClient minioClient, MinIOBucketsNamesProperties buckets) {
        super(minioClient, buckets);
    }

    @Override
    public void downloadZipStreamDirectory(PathDetails detail, OutputStream out) {
        log.debug("Start download objects: {}", detail.getFullPath());
        assertObjectExists(detail);

        List<String> paths = listObjectsByPrefix(detail.getFullPath(), true).stream()
                .map(Item::objectName)
                .map(s -> s.substring(detail.getFullPath().length()))
                .filter(s -> !s.isBlank())
                .sorted()
                .toList();

        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            for (String path : paths) {
                zip.putNextEntry(new ZipEntry(path));
                try (InputStream is = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(buckets.getUser())
                                .object(detail.getFullPath() + path)
                                .build())) {
                    is.transferTo(zip);}
                zip.closeEntry();
            }
            log.debug("Downloading objects successfully: {}, count: {}", detail.getFullPath(), paths.size());
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }

    @Override
    public void downloadZipStreamFile(PathDetails detail, OutputStream out){
        log.debug("Start download object: {}", detail.getFullPath());
        assertObjectExists(detail);

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(buckets.getUser())
                        .object(detail.getFullPath())
                        .build())) {
            is.transferTo(out);
            log.debug("Downloading object successfully: {}", detail.getFullPath());
        } catch (Exception ex) {
            throw new StorageException(ex.getMessage(), ex);
        }
    }
}
