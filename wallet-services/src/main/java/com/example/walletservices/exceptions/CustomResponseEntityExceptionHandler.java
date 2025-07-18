package co.zw.logarithm.coverlinkonboarding.exceptions;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public final ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException ex,
                                                                        WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException(
            DataAccessException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value());
        if (ex instanceof org.springframework.dao.InvalidDataAccessApiUsageException) {
            ex.printStackTrace();
            apiError.setMessage("Error saving record, one or more entries are missing");
            apiError.setDebugMessage(ex.getMessage());
            return buildResponseEntity(apiError);
        }

        apiError.setMessage(ex.getCause().getMessage());
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex) {

        ApiValidationError apiVError = new ApiValidationError();
        apiVError.setMessage(ex.getMessage());
        apiVError.setObject("object");
        apiVError.setField("field");
        apiVError.setRejectedValue("RejValue");
        return buildValidationResponseEntity(apiVError);
    }

    @ExceptionHandler(UnexpectedErrorException.class)
    protected ResponseEntity<Object> handleUnexpectedError(
            UnexpectedErrorException ex) {
        ex.printStackTrace();
        ApiError apiError = new ApiError(HttpStatus.EXPECTATION_FAILED.value());
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getDebugMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Invalid input");
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());

        String message = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message = fieldError.getDefaultMessage();

        }

        apiError.setMessage(message);
        apiError.setDebugMessage(ex.getMessage());

        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Record already exists:- " + ex.getMostSpecificCause().getMessage().split("for")[0]);
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Invalid input, please contact administrator");
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidLoginException.class)
    protected ResponseEntity<Object> handleInvalidLoginException(
            InvalidLoginException ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value());
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ApiError onConstraintValidationException(
            ConstraintViolationException e) {
        System.out.println("ConstraintViolation------------------");
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value());
        e.getConstraintViolations().forEach(violation -> {
            error.setMessage(violation.getPropertyPath().toString().toUpperCase(Locale.ROOT) + " - " + violation.getMessage());
            error.getSubErrors().add(
                    new ApiValidationError(violation.getRootBean().toString(), violation.getPropertyPath().toString(), violation.getInvalidValue(), violation.getMessage()) {
                    });
        });

        return error;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value());
        apiError.setMessage(ex.getLocalizedMessage());
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        ex.printStackTrace();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Invalid request, please check your data and try again");
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex) {

        ApiError apiError = new ApiError(HttpStatus.EXPECTATION_FAILED.value());
        if (ex.getCause() != null && ex.getCause().getCause() instanceof javax.validation.ConstraintViolationException) {

            for (ConstraintViolation violation : ((ConstraintViolationException) ex.getCause().getCause()).getConstraintViolations()) {
                apiError.setMessage(violation.getMessage());

            }
            return buildResponseEntity(apiError);
        }
        ex.printStackTrace();

        apiError.setMessage(ex.getLocalizedMessage());
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatusCode()));
    }

    private ResponseEntity<Object> buildValidationResponseEntity(ApiValidationError apiVError) {
        return new ResponseEntity<>(apiVError, HttpStatus.BAD_REQUEST);
    }


}
