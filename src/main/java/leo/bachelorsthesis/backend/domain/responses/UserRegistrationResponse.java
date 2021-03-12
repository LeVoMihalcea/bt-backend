package leo.bachelorsthesis.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponse implements Serializable {
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String message;
}
