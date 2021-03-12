package leo.bachelorsthesis.backend.service.UserService;

import leo.bachelorsthesis.backend.builders.DtoBuilder;
import leo.bachelorsthesis.backend.constants.UserConstants;
import leo.bachelorsthesis.backend.domain.User;
import leo.bachelorsthesis.backend.domain.dtos.UserDto;
import leo.bachelorsthesis.backend.domain.responses.UserRegistrationResponse;
import leo.bachelorsthesis.backend.error.exceptions.UserNotFoundException;
import leo.bachelorsthesis.backend.repository.UserRepository;
import leo.bachelorsthesis.backend.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final DtoBuilder dtoBuilder;

    public UserServiceImpl(UserRepository userRepository, DtoBuilder dtoBuilder) {
        this.userRepository = userRepository;
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> {throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);});
    }

    @Override
    public UserRegistrationResponse register(UserDto userDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        List<User> leo = userRepository.findByFirstName("Leo");

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        return dtoBuilder.buildUserRegistrationResponse(savedUser);
    }

    @Override
    public Optional<String> login(String email, String password) {
        return Optional.empty();
    }

    @Override
    public void logout(int id) {

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> {throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);});
    }
}
