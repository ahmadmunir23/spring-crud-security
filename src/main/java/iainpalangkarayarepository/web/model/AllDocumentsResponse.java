package iainpalangkarayarepository.web.model;

import iainpalangkarayarepository.web.entity.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllDocumentsResponse {
    List<Document> documents;
}
