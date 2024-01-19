package iainpalangkarayarepository.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.LoginUserRequest;
import iainpalangkarayarepository.web.model.TokenResponse;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.repository.DocumentRepository;
import iainpalangkarayarepository.web.repository.UserRepository;
import iainpalangkarayarepository.web.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    
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
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = LoginUserRequest
                .builder()
                .username("test")
                .password("test")
                .build();

        mockMvc.perform(
                post("/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            System.out.println(response.getErrors());
        });
    }

    @Test
    void loginFailedWrongPassword() throws Exception {
        User user = User
                .builder()
                .username("test")
                .password(BCrypt.hashpw("test", BCrypt.gensalt()))
                .build();
        userRepository.save(user);

        LoginUserRequest request = LoginUserRequest
                .builder()
                .username("test")
                .password("test123")
                .build();

        mockMvc.perform(
                post("/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            System.out.println(response.getErrors());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        User user = User
                .builder()
                .username("test")
                .password(BCrypt.hashpw("test", BCrypt.gensalt()))
                .build();
        userRepository.save(user);

        LoginUserRequest request = LoginUserRequest
                .builder()
                .username("test")
                .password("test123")
                .build();

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
        });
    }
}