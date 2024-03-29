package iainpalangkarayarepository.web.repository;

import iainpalangkarayarepository.web.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, String> {
    
    
    @Query(
            value = """
            select d.id, d.created_at, d.updated_at, d.title, d.url, u.username FROM documents d JOIN users u ON (d.username = u.username)
            """,
            nativeQuery = true
    )
    List<Document> getAllDocuments();
}
