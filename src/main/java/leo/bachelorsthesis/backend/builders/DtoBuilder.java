package leo.bachelorsthesis.backend.builders;

import leo.bachelorsthesis.backend.domain.User;
import leo.bachelorsthesis.backend.domain.responses.UserRegistrationResponse;

public class DtoBuilder {


    public UserRegistrationResponse buildUserRegistrationResponse(User user) {
        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        if (user.getId() != 0) {
            response.setId(user.getId());
            response.setMessage("User added successfully");
        } else {
            response.setMessage("There was an error!");
        }

        return response;
    }
}
