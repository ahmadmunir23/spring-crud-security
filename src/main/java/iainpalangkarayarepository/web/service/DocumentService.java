package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.Document;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ValidationService falidationSerfice;

    @Transactional
    public DocumentResponse create(User user, @RequestBody CreateDocumentRequest request) {
        falidationSerfice.validate(request);

        Document document = Document
                .builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle())
                .url(UUID.randomUUID().toString())
                .user(user)
                .build();
        documentRepository.save(document);

        return toDocumentResponse(document);

    }

    private DocumentResponse toDocumentResponse(Document document) {
        return DocumentResponse.builder()
                .title(document.getTitle())
                .url(document.getUrl())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
