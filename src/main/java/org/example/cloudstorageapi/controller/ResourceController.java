package org.example.cloudstorageapi.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.controller.api.ResourceApi;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.security.CustomUserDetails;
import org.example.cloudstorageapi.service.storage.StorageDownloadService;
import org.example.cloudstorageapi.service.storage.StorageResourceService;
import org.example.cloudstorageapi.service.storage.StorageUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/resource", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ResourceController implements ResourceApi<CustomUserDetails> {

    private final StorageResourceService<RespResourceInfoDTO, Long> resourceService;
    private final StorageUploadService<RespResourceInfoDTO, Long> uploadService;
    private final StorageDownloadService<Long> downloadService;

    @GetMapping
    public ResponseEntity<RespResourceInfoDTO> getInfo(@RequestParam("path") String path,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        RespResourceInfoDTO info = resourceService.getObjectInfo(userDetails.getId(), path);
        return ResponseEntity.ok(info);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("path") String path,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        resourceService.remove(userDetails.getId(), path);
    }

    @Override
    @GetMapping("/move")
    public ResponseEntity<RespResourceInfoDTO> moveOrRename(@RequestParam("from") String pathFrom,
                                                            @RequestParam("to") String pathTo,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        RespResourceInfoDTO info = resourceService.moveOrRename(userDetails.getId(), pathFrom, pathTo);
        return ResponseEntity.ok(info);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<RespResourceInfoDTO>> search(@RequestParam("query") String query,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RespResourceInfoDTO> resources = resourceService.searchObjects(userDetails.getId(), query);
        return ResponseEntity.ok(resources);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<RespResourceInfoDTO>> upload(@RequestParam("path") String path,
                                                            @RequestPart("object") List<MultipartFile> multipartFile,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        List<RespResourceInfoDTO> upload = uploadService.upload(userDetails.getId(), path, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(upload);
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public void download(@RequestParam("path") String path,
                         HttpServletResponse response,
                         @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        downloadService.download(userDetails.getId(), path, response.getOutputStream());
    }
}
