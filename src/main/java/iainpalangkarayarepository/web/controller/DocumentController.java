package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.entity.Document;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.AllDocumentsResponse;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.repository.UserRepository;
import iainpalangkarayarepository.web.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    
    private final DocumentService documentSerfice;
    

    @PostMapping(
            path = "/documents",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DocumentResponse> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateDocumentRequest request) {
        DocumentResponse documentResponse = documentSerfice.create(userDetails, request);
        return WebResponse.<DocumentResponse>builder().data(documentResponse).build();
    }
    
    @GetMapping(
            path = "/documents",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AllDocumentsResponse> getOne() {
        AllDocumentsResponse allDocumentsResponse = new AllDocumentsResponse();
        List<Document> documentResponse = documentSerfice.getAll();
        allDocumentsResponse.setDocuments(documentResponse);
        return new ResponseEntity<>(allDocumentsResponse, HttpStatus.OK);
    }

}
