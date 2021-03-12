package leo.bachelorsthesis.backend.service.UserService;

import leo.bachelorsthesis.backend.domain.User;
import leo.bachelorsthesis.backend.domain.dtos.UserDto;
import leo.bachelorsthesis.backend.domain.responses.UserRegistrationResponse;

import java.util.Optional;

public interface UserService {
    User getUserById(int id);

    User findByEmail(String email);

    UserRegistrationResponse register(UserDto userDto);

    Optional<String> login(String email, String password);

    void logout(int id);
}
