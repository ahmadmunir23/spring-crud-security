package iainpalangkarayarepository.web.service;

import iainpalangkarayarepository.web.entity.User;
import iainpalangkarayarepository.web.model.RegisterUserRequest;
import iainpalangkarayarepository.web.model.TokenResponse;
import iainpalangkarayarepository.web.model.UpdateUserRequest;
import iainpalangkarayarepository.web.model.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();

    UserResponse getOne(String username);

    TokenResponse register(RegisterUserRequest request);

    UserResponse update(
            String username,
            UpdateUserRequest request
    );

    void delete(String request);
}
