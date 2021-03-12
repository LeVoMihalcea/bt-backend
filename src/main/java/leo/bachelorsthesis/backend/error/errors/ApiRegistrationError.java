package leo.bachelorsthesis.backend.error.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiRegistrationError extends ApiSubError {
    private String object;
    private String field;
    private String message;

    public ApiRegistrationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiRegistrationError(String object, String field, String message) {
        this.object = object;
        this.field = field;
        this.message = message;
    }
}
