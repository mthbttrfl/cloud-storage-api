package org.example.cloudstorageapi.integration;

import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.exception.UsernameAlreadyExists;
import org.example.cloudstorageapi.model.UserEntity;
import org.example.cloudstorageapi.repository.UserRepository;
import org.example.cloudstorageapi.service.user.UserRegistrationServiceImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("UserRegistrationService")
public class UserRegistrationServiceIntegrationTest extends IntegrationTestContainers{

    @Autowired
    private UserRegistrationServiceImpl registrationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registration_createsUserInDatabase() {
        ReqUserDTO dto = new ReqUserDTO("usernameTest", "Password@1");

        registrationService.registration(dto);

        Optional<UserEntity> savedUser = userRepository.findByUsername("usernameTest");

        assertTrue(savedUser.isPresent());
        assertEquals("usernameTest", savedUser.get().getUsername());
        assertNotEquals("Password@1", savedUser.get().getPassword());
    }

    @Test
    void registration_duplicateUsername_throwsException() {
        ReqUserDTO dto = new ReqUserDTO("usernameTest", "Password@1");

        registrationService.registration(dto);

        assertThrows(UsernameAlreadyExists.class, () ->
                registrationService.registration(dto)
        );
    }
}
