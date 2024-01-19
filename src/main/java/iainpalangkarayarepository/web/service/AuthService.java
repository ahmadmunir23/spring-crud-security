package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.Token;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.LoginUserRequest;
import iainpalangkarayarepository.web.model.TokenResponse;
import iainpalangkarayarepository.web.model.TokenType;
import iainpalangkarayarepository.web.repository.TokenRepository;
import iainpalangkarayarepository.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    
    private final JwtService jwtService;
    
    private final UserRepository userRepository;
    
    private final TokenRepository tokenRepository;
    
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            String jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            
            return TokenResponse.builder()
                    .token(jwtToken)
                    .expiredAt(System.currentTimeMillis() + jwtExpiration)
                    .build();
        } catch (Exception exception) {
            throw new UsernameNotFoundException(exception.getMessage());
        }
    }
    
    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUsername());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
    
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

}
