package org.example.cloudstorageapi.controller;

import org.example.cloudstorageapi.controller.api.UserApi;
import org.example.cloudstorageapi.dto.resp.RespUsernameDTO;
import org.example.cloudstorageapi.security.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController implements UserApi<CustomUserDetails> {

    @Override
    @GetMapping("/me")
    public ResponseEntity<RespUsernameDTO> me(@AuthenticationPrincipal CustomUserDetails userDetails ) {
        return ResponseEntity.ok(new RespUsernameDTO(userDetails.getUsername()));
    }
}
