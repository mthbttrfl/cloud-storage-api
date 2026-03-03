package org.example.cloudstorageapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.controller.api.AuthApi;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.dto.resp.RespUsernameDTO;
import org.example.cloudstorageapi.service.user.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserAccountService<Long, ReqUserDTO> userAccountService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<RespUsernameDTO> signUp(@RequestBody @Validated ReqUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        userAccountService.registerAndInitRootFolder(userDTO);
        Authentication authenticated = userAccountService.authenticate(userDTO, request, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RespUsernameDTO(authenticated.getName()));
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<RespUsernameDTO> signIn(@RequestBody @Validated ReqUserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticated = userAccountService.authenticate(userDTO, request, response);
        return ResponseEntity.status(HttpStatus.OK).body(new RespUsernameDTO(authenticated.getName()));
    }
}
