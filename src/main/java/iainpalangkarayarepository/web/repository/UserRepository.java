package iainpalangkarayarepository.web.repository;

import iainpalangkarayarepository.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> deleteByUsername(String username);

    Optional<User> findByUsername(String username);
}
