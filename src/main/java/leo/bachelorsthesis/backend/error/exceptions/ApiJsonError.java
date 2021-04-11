package leo.bachelorsthesis.backend.error.exceptions;

import leo.bachelorsthesis.backend.error.errors.ApiSubError;

public class ApiJsonError extends RuntimeException{
    public ApiJsonError(String message) {
        super(message);
    }
}
