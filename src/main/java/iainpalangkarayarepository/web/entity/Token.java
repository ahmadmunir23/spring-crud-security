package iainpalangkarayarepository.web.entity;

import iainpalangkarayarepository.web.model.TokenType;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String token;
    
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    
    private boolean expired;
    
    private boolean revoked;
    
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
