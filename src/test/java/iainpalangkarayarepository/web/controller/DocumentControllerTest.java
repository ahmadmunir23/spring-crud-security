package iainpalangkarayarepository.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
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
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
        userRepository.deleteAll();

        User user = User
                .builder()
                .name("test")
                .username("test")
                .password(BCrypt.hashpw("test", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
        userRepository.save(user);
    }

    @Test
    void createFailed() throws Exception {
        CreateDocumentRequest request = CreateDocumentRequest
                .builder()
                .title("")
                .build();

        mockMvc.perform(
                post("/documents")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            System.out.println(response.getErrors());
        });
    }

    @Test
    void createSuccess() throws Exception {
        CreateDocumentRequest request = CreateDocumentRequest
                .builder()
                .title("Document 1")
                .build();

        mockMvc.perform(
                post("/documents")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<DocumentResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            System.out.println(response.getData().getTitle());
            System.out.println(response.getData().getUrl());
            System.out.println(response.getData().getCreatedAt());
            System.out.println(response.getData().getUpdatedAt());
        });
    }
}