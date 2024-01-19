package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.service.JwtService;
import iainpalangkarayarepository.web.model.LoginUserRequest;
import iainpalangkarayarepository.web.model.TokenResponse;
import iainpalangkarayarepository.web.model.WebResponse;
import iainpalangkarayarepository.web.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authSerfice;
    private final JwtService jwtUtil;
    

    @PostMapping(
            path = "/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        try {
            TokenResponse tokenResponse = authSerfice.login(request);
            
            return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
        } catch (UsernameNotFoundException exception) {
            throw exception;
        }


//    @DeleteMapping(
//            path = "/auth/logout",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public WebResponse<String> logout(User user) {
//        authSerfice.logout(user);
//        return WebResponse.<String>builder().data("OK").build();
//    }
    }
}
