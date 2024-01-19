package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentSerfice;

    @PostMapping(
            path = "/documents",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DocumentResponse> create(User user, @RequestBody CreateDocumentRequest request) {
        DocumentResponse documentResponse = documentSerfice.create(user, request);
        return WebResponse.<DocumentResponse>builder().data(documentResponse).build();
    }

}
