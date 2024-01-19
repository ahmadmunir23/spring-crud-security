package iainpalangkarayarepository.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.RegisterUserRequest;
import iainpalangkarayarepository.web.model.UpdateUserRequest;
import iainpalangkarayarepository.web.model.UserResponse;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.repository.DocumentRepository;
import iainpalangkarayarepository.web.repository.UserRepository;
import iainpalangkarayarepository.web.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;
    

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerSuccess() throws Exception {
        RegisterUserRequest request = RegisterUserRequest
                .builder()
                .name("Munir")
                .username("munir77")
                .password("munir123")
                .build();

        mockMvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void registerBadRequest() throws Exception {
        RegisterUserRequest request = RegisterUserRequest
                .builder()
                .name("")
                .username("")
                .password("")
                .build();

        mockMvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAllSuccess() throws Exception {
        User user = User
                .builder()
                .name("Munir")
                .username("munirk77")
                .password(BCrypt.hashpw("munir123", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
        userRepository.save(user);

        mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            log.info("Users : ", response.getData().size());
        });
    }

    @Test
    void getAllUnauthorized() throws Exception {
        mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateSuccess() throws Exception {
        User user = User
                .builder()
                .name("Munir")
                .username("munirk77")
                .password(BCrypt.hashpw("munir123", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
        userRepository.save(user);

        UpdateUserRequest request = UpdateUserRequest
                .builder()
                .name("Ahmad Munir")
                .username("munir123")
                .build();

        mockMvc.perform(
                patch("/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")
                        .content(objectMapper.writeValueAsString(request))

        );
    }

    @Test
    void updateBadRequest() throws Exception {
        User user = User
                .builder()
                .name("Munir")
                .username("munirk77")
                .password(BCrypt.hashpw("munir123", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
        userRepository.save(user);

        UpdateUserRequest request = UpdateUserRequest
                .builder()
                .name("r")
                .username("")
                .build();

        mockMvc.perform(
                patch("/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")
                        .content(objectMapper.writeValueAsString(request))

        );
    }
}