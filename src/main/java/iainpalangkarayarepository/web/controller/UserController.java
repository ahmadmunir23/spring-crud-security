package iainpalangkarayarepository.web.controller;

import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.*;
import iainpalangkarayarepository.web.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    // CREATE
    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> register(@RequestBody RegisterUserRequest request) {
        TokenResponse tokenResponse = userService.register(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    //GET ALL
    @GetMapping(
            path = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> getAll() {
            List<UserResponse> usersResponse = userService.getAll();
            return WebResponse.<List<UserResponse>>builder().data(usersResponse).build();
    }

    @GetMapping(
            path = "/users/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getOne(
            @PathVariable(name = "username") String username ) {

        UserResponse userResponse = userService.getOne(username);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }


    // UPDATE
    @PatchMapping(
            path = "/users/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(
            @PathVariable(name = "username") String username, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(username, request);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    // DELETE
    @DeleteMapping(
            path = "/users/{username}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteUser(@PathVariable(name = "username") String username) {
        userService.delete(username);

        return WebResponse.<String>builder().data("OK").build();
    }


}
