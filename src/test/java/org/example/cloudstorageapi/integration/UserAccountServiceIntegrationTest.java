package org.example.cloudstorageapi.integration;

import io.minio.StatObjectResponse;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.model.UserEntity;
import org.example.cloudstorageapi.repository.UserRepository;
import org.example.cloudstorageapi.service.user.UserAccountServiceImpl;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.minio.operation.MinIOStorageResourceOperations;
import org.example.cloudstorageapi.storage.minio.resolver.MinIOVirtualPathWithRootFolderResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Tag("UserAccountService")
public class UserAccountServiceIntegrationTest extends IntegrationTestContainers{

    @Autowired
    private UserAccountServiceImpl userAccountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinIOVirtualPathWithRootFolderResolver<Long> resolver;

    @Autowired
    private MinIOStorageResourceOperations storageResourceOperations;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach()
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registration_success() {
        ReqUserDTO dto = new ReqUserDTO("usernameTest", "Password@1");
        Long registrationId = userAccountService.registration(dto);

        Optional<UserEntity> byId = userRepository.findById(registrationId);
        assertTrue(byId.isPresent());
        UserEntity user = byId.get();
        assertEquals(registrationId, user.getId());
        assertEquals(dto.getUsername(), user.getUsername());
        assertNotEquals(dto.getPassword(), user.getPassword());
        assertTrue(passwordEncoder.matches(dto.getPassword(), user.getPassword()));
    }

    @Test
    void authenticate_success() {
        ReqUserDTO dto = new ReqUserDTO("usernameTest", "Password@1");
        userAccountService.registration(dto);

        Authentication authenticated = userAccountService.authenticate(dto, request, response);

        assertTrue(authenticated.isAuthenticated());
        assertEquals(dto.getUsername(), authenticated.getName());
        assertSame(authenticated, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void initRootFolder_success() {
        ReqUserDTO dto = new ReqUserDTO("usernameTest", "Password@1");
        Long registrationId = userAccountService.registration(dto);
        userAccountService.initRootFolder(registrationId);

        PathDetails resolve = resolver.resolve(registrationId, "");
        StatObjectResponse objectInfo = storageResourceOperations.getObjectInfo(resolve);

        assertEquals(resolve.getRootFolder(), objectInfo.object());
    }
}