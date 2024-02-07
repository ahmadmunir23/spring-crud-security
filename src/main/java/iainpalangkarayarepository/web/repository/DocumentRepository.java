package iainpalangkarayarepository.web.repository;

import iainpalangkarayarepository.web.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, String> {
    
    
    @Query(
            value = """
            select * from documents
            """,
            nativeQuery = true
    )
    List<Document> getAllDocuments();
}
