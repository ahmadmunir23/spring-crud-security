package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.Document;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.model.UpdateDocumentRequest;
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
import java.util.stream.Collectors;

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
    
    public List<DocumentResponse> getAll() {
        List<Document> documents = documentRepository.getAllDocuments();
        return toDocumentResponse(documents);
    }
    
    public DocumentResponse getOne(String uuId) {
        
        Document document = documentRepository.findById(uuId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        return toDocumentResponse(document);
    }
    
    @Transactional
    public DocumentResponse updateOne(String uuId, UpdateDocumentRequest request) {
        Document document = documentRepository.findById(uuId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        
        document.setTitle(request.getTitle());
        documentRepository.save(document);
        return toDocumentResponse(document);
    }
    
    @Transactional
    public void deleteOne(String uuId) {
        documentRepository.deleteById(uuId);
    }
    
    private DocumentResponse toDocumentResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .url(document.getUrl())
                .createdAt(new Date().toInstant())
                .updatedAt(document.getUpdatedAt())
                .user(document.getUser().getUsername())
                .build();
    }
    
    private List<DocumentResponse> toDocumentResponse(List<Document> documents) {
        return documents.stream()
                .map(document -> DocumentResponse
                        .builder()
                        .user(document.getUser().getUsername())
                        .title(document.getTitle())
                        .id(document.getId())
                        .createdAt(document.getCreatedAt())
                        .updatedAt(document.getUpdatedAt())
                        .url(document.getUrl())
                        .build()
        ).collect(Collectors.toList());
        
        
    }
    
}
