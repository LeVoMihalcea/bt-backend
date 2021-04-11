package leo.bachelorsthesis.backend.controllers;

import leo.bachelorsthesis.backend.domain.User;
import leo.bachelorsthesis.backend.domain.dtos.UserDto;
import leo.bachelorsthesis.backend.domain.responses.UserRegistrationResponse;
import leo.bachelorsthesis.backend.service.UserService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserRegistrationResponse registerUser(@RequestBody @Valid UserDto userDto){
        logger.info("received registration request: {}", userDto);
        UserRegistrationResponse userRegistrationResponse = this.userService.register(userDto);
        logger.info("registration done: {}", userRegistrationResponse);
        return userRegistrationResponse;
    }

    @GetMapping("{email}")
    public User getUserDetails(@PathVariable String email){
        logger.info("received get user details request: {}", email);
        User user = this.userService.findByEmail(email);
        logger.info("registration done: {}", user);
        return user;
    }
}
