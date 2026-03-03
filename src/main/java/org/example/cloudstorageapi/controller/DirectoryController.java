package org.example.cloudstorageapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.controller.api.DirectoryApi;
import org.example.cloudstorageapi.dto.resp.RespResourceInfoDTO;
import org.example.cloudstorageapi.security.CustomUserDetails;
import org.example.cloudstorageapi.service.storage.StorageDirectoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/directory", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DirectoryController implements DirectoryApi<CustomUserDetails> {

    private final StorageDirectoryService<RespResourceInfoDTO, Long> service;

    @Override
    @GetMapping
    public ResponseEntity<List<RespResourceInfoDTO>> getObjectsInfo(@RequestParam("path") String path, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RespResourceInfoDTO> objects = service.getObjects(userDetails.getId(), path);
        return ResponseEntity.status(HttpStatus.OK.value()).body(objects);
    }

    @Override
    @PostMapping
    public ResponseEntity<RespResourceInfoDTO> create(@RequestParam("path") String path, @AuthenticationPrincipal CustomUserDetails userDetails) {
        RespResourceInfoDTO resourceInfo = service.create(userDetails.getId(), path);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(resourceInfo);
    }
}
