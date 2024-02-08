package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.model.CreateDocumentRequest;
import iainpalangkarayarepository.web.model.DocumentResponse;
import iainpalangkarayarepository.web.model.UpdateDocumentRequest;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public WebResponse<DocumentResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateDocumentRequest request) {
        DocumentResponse documentResponse = documentSerfice.create(userDetails, request);
        return WebResponse.<DocumentResponse>builder().data(documentResponse).build();
    }
    
    @GetMapping(
            path = "/documents",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<DocumentResponse>> getAll() {
        List<DocumentResponse> documentResponse = documentSerfice.getAll();
        return WebResponse.<List<DocumentResponse>>builder().data(documentResponse).build();
    }
    
    @GetMapping(
            path = "/documents/{uuId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DocumentResponse> getOne(@PathVariable(name = "uuId") String uuId) {
        DocumentResponse documentResponse = documentSerfice.getOne(uuId);
        return WebResponse.<DocumentResponse>builder().data(documentResponse).build();
    }
    
    @PatchMapping(
            path = "/documents/{uuId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DocumentResponse> updateOne(
            @PathVariable(name = "uuId") String uuId,
            @RequestBody UpdateDocumentRequest request) {
        DocumentResponse documentResponse = documentSerfice.updateOne(uuId, request);
        return WebResponse.<DocumentResponse>builder().data(documentResponse).build();
    }
    
    @DeleteMapping(
            path = "/documents/{uuId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    WebResponse<String> deleteOne(@PathVariable(name = "uuId") String uuId) {
        documentSerfice.deleteOne(uuId);
        return WebResponse.<String>builder().data("OK").build();
    }

}
