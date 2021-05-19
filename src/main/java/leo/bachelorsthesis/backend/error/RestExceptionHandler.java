package leo.bachelorsthesis.backend.error;

import leo.bachelorsthesis.backend.constants.GeneralConstants;
import leo.bachelorsthesis.backend.constants.UserConstants;
import leo.bachelorsthesis.backend.constants.ValidationConstants;
import leo.bachelorsthesis.backend.error.errors.ApiError;
import leo.bachelorsthesis.backend.error.errors.ApiRegistrationError;
import leo.bachelorsthesis.backend.error.errors.ApiSubError;
import leo.bachelorsthesis.backend.error.errors.ApiValidationError;
import leo.bachelorsthesis.backend.error.exceptions.APIAuthenticationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 5050)
public class RestExceptionHandler {

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        List<ApiSubError> apiSubErrorList = new ArrayList<>();

        if (Objects.requireNonNull(exception.getMessage()).contains("uc_email_unique")) {
            apiSubErrorList.add(
                    new ApiRegistrationError(
                            "User",
                            "Email Address",
                            UserConstants.UNIQUE_EMAIL_ERROR_MESSAGE
                    )
            );
        }

        return buildResponseEntity(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        GeneralConstants.SOMETHING_WENT_WRONG,
                        GeneralConstants.SOMETHING_WENT_WRONG,
                        apiSubErrorList
                )
        );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ApiValidationError> apiSubErrorList = Optional.of(exception)
                .map(BindException::getBindingResult)
                .map(Errors::getAllErrors)
                .map(objectErrors ->
                        objectErrors.stream()
                                .filter(objectError -> objectError instanceof FieldError)
                                .map(objectError -> (FieldError) objectError)
                                .map(objectError ->
                                        ApiValidationError.builder()
                                                .object(objectError.getObjectName())
                                                .field(objectError.getField())
                                                .message(objectError.getDefaultMessage())
                                                .rejectedValue(objectError.getRejectedValue())
                                                .build()
                                ).collect(Collectors.toList())
                ).orElse(List.of());

        return buildResponseEntity(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        ValidationConstants.VALIDATION_ERROR,
                        "validation_exception",
                        new ArrayList<>(apiSubErrorList)
                )
        );
    }

    @ExceptionHandler(value = {APIAuthenticationException.class})
    protected ResponseEntity<Object> handleSignatureException(APIAuthenticationException exception) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, exception.getMessage(),
                "unsigned_jwt_token", new ArrayList<>());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGeneralException(RuntimeException exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(),
                "something_went_wrong", new ArrayList<>());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
