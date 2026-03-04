package org.example.cloudstorageapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.example.cloudstorageapi.dto.req.ReqUserDTO;
import org.example.cloudstorageapi.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerIntegrationTest extends IntegrationTestContainers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Cookie user1Cookie;
    private Cookie user2Cookie;

    @BeforeEach
    void authenticateUsers() throws Exception {
        user1Cookie = signUpAndLogin("userOne", "Password@1");
        user2Cookie = signUpAndLogin("userTwo", "Password@2");
    }

    private Cookie signUpAndLogin(String username, String password) throws Exception {
        ReqUserDTO dto = new ReqUserDTO(username, password);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        MvcResult signInResult = mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        Cookie cookie = signInResult.getResponse().getCookie("SESSION");
        assertThat(cookie).isNotNull();
        return cookie;
    }

    private void uploadFile(String filename, Cookie cookie) throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "object",
                filename,
                MediaType.TEXT_PLAIN_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/resource")
                                .file(file)
                                .param("path", "/")
                                .cookie(cookie)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Переименование файла успешно")
    void rename_file_success() throws Exception {
        uploadFile("old.txt", user1Cookie);

        mockMvc.perform(
                        get("/api/resource/move")
                                .param("from", "old.txt")
                                .param("to", "new.txt")
                                .cookie(user1Cookie)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new.txt"))
                .andExpect(jsonPath("$.type").value(ResourceType.FILE.toString()));

        mockMvc.perform(
                        get("/api/resource")
                                .param("path", "old.txt")
                                .cookie(user1Cookie)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удаление файла успешно")
    void delete_file_success() throws Exception {
        uploadFile("delete.txt", user1Cookie);

        mockMvc.perform(
                        delete("/api/resource")
                                .param("path", "delete.txt")
                                .cookie(user1Cookie)
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get("/api/resource")
                                .param("path", "delete.txt")
                                .cookie(user1Cookie)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Пользователь не может получить доступ к чужим файлам")
    void user_cannot_access_foreign_files() throws Exception {
        uploadFile("secret.txt", user1Cookie);

        mockMvc.perform(
                        get("/api/resource")
                                .param("path", "secret.txt")
                                .cookie(user2Cookie)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Поиск возвращает только файлы текущего пользователя")
    void search_returns_only_user_files() throws Exception {
        uploadFile("file-user1.txt", user1Cookie);
        uploadFile("file-user2.txt", user2Cookie);

        mockMvc.perform(
                        get("/api/resource/search")
                                .param("query", "file")
                                .cookie(user1Cookie)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'file-user1.txt')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'file-user2.txt')]").doesNotExist());
    }
}