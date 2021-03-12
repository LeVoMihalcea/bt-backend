package leo.bachelorsthesis.backend.error.exceptions;

public class APIAuthenticationException extends RuntimeException {
    public APIAuthenticationException(String message) {
        super(message);
    }
}
