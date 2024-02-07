package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.Document;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.repository.DocumentRepository;
import iainpalangkarayarepository.web.repository.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    
    private final ValidationService falidationSerfice;
    
    private final UserRepository userRepository;

    @Transactional
    public DocumentResponse create(UserDetails userDetails, @RequestBody CreateDocumentRequest request) {
        falidationSerfice.validate(request);

        User user = userRepository.findById(userDetails.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Document document = Document
                .builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle())
                .url(UUID.randomUUID().toString())
                .user(user)
                .build();
        System.out.println(document);
        documentRepository.save(document);
        
        System.out.println("Created At : " + document.getCreatedAt());

        return toDocumentResponse(document);

    }
    
    public List<Document> getAll() {
        return documentRepository.getAllDocuments();
    }
    
    private DocumentResponse toDocumentResponse(Document document) {
        return DocumentResponse.builder()
                .title(document.getTitle())
                .url(document.getUrl())
                .createdAt(new Date().toInstant())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
    
    private List<DocumentResponse> toDocumentResponse(List<Document> documents) {
        List<DocumentResponse> allDocuments = new ArrayList<>();
        documents.forEach(document -> {
            allDocuments.add(
                    DocumentResponse.builder()
                            .title(document.getTitle())
                            .url(document.getUrl())
                            .createdAt(document.getCreatedAt())
                            .updatedAt(document.getUpdatedAt())
                            .build()
            );
        });
        
        return allDocuments;
    }
    

}
