package iainpalangkarayarepository.web.repository;

import iainpalangkarayarepository.web.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(
            value = """
            select t1.id, t1.token_type, t1.token, t1.expired, t1.revoked, t1.username, t2.role from tokens t1 inner join users t2 on t1.username = t2.username where t2.username = :idUsername and (t1.expired = false or t1.revoked = false)
            """,
            nativeQuery = true
    )
    List<Token> findAllValidTokensByUser(@Param("idUsername") String username);
    
    @Query(
            value = """
                    select t.id, t.token_type, t.token, t.expired, t.revoked, t.username, u.role from tokens t inner join users u on t.username = u.username where u.username = :username
                    """,
            nativeQuery = true
    )
    List<Token> findByUsername(String username);
    
    Optional<Token> findByToken(String token);
}
