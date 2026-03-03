package org.example.cloudstorageapi.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.service.auth.AuthenticationService;
import org.example.cloudstorageapi.service.storage.RootFolderInitializerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService<Long, ReqUserDTO> {

    private final UserRegistrationService<ReqUserDTO, Long> userRegistrationService;
    private final AuthenticationService<ReqUserDTO> authenticationService;
    private final RootFolderInitializerService<Long> rootFolderInitializerService;


    @Override
    public void registerAndInitRootFolder(ReqUserDTO userDTO) {
        Long registrationId = registration(userDTO);
        initRootFolder(registrationId);
    }

    @Override
    public void initRootFolder(Long id) {
        rootFolderInitializerService.init(id);
    }

    public Long registration(ReqUserDTO userDTO){
        return userRegistrationService.registration(userDTO);
    }

    public Authentication authenticate(ReqUserDTO userDTO, HttpServletRequest request, HttpServletResponse response){
        return authenticationService.authenticate(userDTO, request, response);
    }
}
