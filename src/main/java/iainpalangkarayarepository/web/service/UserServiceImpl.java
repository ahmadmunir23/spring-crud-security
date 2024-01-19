package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.Token;
import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.*;
import iainpalangkarayarepository.web.repository.TokenRepository;
import iainpalangkarayarepository.web.repository.UserRepository;
import iainpalangkarayarepository.web.security.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    
    private final UserRepository userRepository;
    
    private final TokenRepository tokenRepository;
    
    private final ValidationService validationService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;
    
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(item -> toUserResponse(item)).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getOne(String username) {
        User user = userRepository.findById(username).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .build();
    }

    @Transactional
    public TokenResponse register(RegisterUserRequest request) {
        validationService.validate(request);
        
        Role role = null;
        if (request.getRole().name().equals("ADMIN")) {
            role = Role.ADMIN;
        }
        
        if (request.getRole().name().equals("USER")) {
            role = Role.USER;
        }

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        
        System.out.println(request.getRole());

        User user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .role(role)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        
        return TokenResponse.builder()
                .token(jwtToken)
                .expiredAt(System.currentTimeMillis() + jwtExpiration)
                .build();
    }

    @Transactional
    public UserResponse update(String username, UpdateUserRequest request) {
        validationService.validate(request);

        System.out.println("Username : " + username);
        System.out.println("Request from user : " + request);
        
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        System.out.println("User : " + user);
        
        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            // user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        System.out.println(user);

        return toUserResponse(user);
    }


    @Transactional
    public void delete(String username) {
        List<Token> userTokens = tokenRepository.findByUsername(username);

        tokenRepository.deleteAll(userTokens);
        
        userRepository.deleteByUsername(username).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse
                .builder()
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
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

