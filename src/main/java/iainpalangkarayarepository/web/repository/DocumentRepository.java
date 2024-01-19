package iainpalangkarayarepository.web.repository;

import iainpalangkarayarepository.web.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {

}
