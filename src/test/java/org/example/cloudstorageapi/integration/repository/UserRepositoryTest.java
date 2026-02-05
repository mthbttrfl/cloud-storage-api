package org.example.cloudstorageapi.integration.repository;

import org.example.cloudstorageapi.entity.UserModel;
import org.example.cloudstorageapi.integration.IntegrationTestContainers;
import org.example.cloudstorageapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends IntegrationTestContainers {

    private final UserModel testUser = UserModel.builder()
            .username("test1")
            .password("test1")
            .build();

    @Autowired
    private UserRepository userRepository;


    @Test
    void findByUsername(){
        Optional<UserModel> testByUsername = userRepository.findByUsername(testUser.getUsername());

        assertTrue(testByUsername.isPresent());
        assertEquals(testUser.getUsername(), testByUsername.get().getUsername());
        assertEquals(testUser.getPassword(), testByUsername.get().getPassword());
    }

    @Test
    void uniqueConstrainUsername(){
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(testUser));
    }
}
