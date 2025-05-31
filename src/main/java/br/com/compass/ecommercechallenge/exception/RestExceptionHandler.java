package br.com.compass.ecommercechallenge.exception;

import br.com.compass.ecommercechallenge.exception.message.RestErrorMessage;
import br.com.compass.ecommercechallenge.exception.message.ValidationErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<?> notFoundHandler(NotFoundException exception, HttpServletRequest request) {
        var errorStatus = HttpStatus.NOT_FOUND;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(exception.getMessage())
                .instance(request.getRequestURI()).build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.FORBIDDEN;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail("Access denied: you do not have permission to perform this action.")
                .instance(request.getRequestURI()).build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RestErrorMessage> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.UNAUTHORIZED;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail("Authentication failed: " + ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<RestErrorMessage> handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.UNAUTHORIZED;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<RestErrorMessage> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.BAD_REQUEST;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<RestErrorMessage> handleSamePasswordException(SamePasswordException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        var validationErrorMessage = ValidationErrorMessage
                .builder()
                .title(ex.getBody().getTitle())
                .status(status.value())
                .details(errors)
                .instance(((ServletWebRequest)request).getRequest().getRequestURI())
                .build();
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(validationErrorMessage);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<RestErrorMessage> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.BAD_REQUEST;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InvalidUuidFormatException.class)
    public ResponseEntity<RestErrorMessage> handleInvalidUuidFormat(InvalidUuidFormatException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.BAD_REQUEST;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(ProductAssociatedWithOrderException.class)
    public ResponseEntity<RestErrorMessage> handleProductAssociatedWithOrderException(ProductAssociatedWithOrderException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.CONFLICT;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InactivateProductException.class)
    public ResponseEntity<RestErrorMessage> handleProductInactivateProductException(InactivateProductException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.CONFLICT;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<RestErrorMessage> handleInsufficientStockException(InsufficientStockException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.CONFLICT;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<RestErrorMessage> handleEmptyCartException(EmptyCartException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.BAD_REQUEST;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }

    @ExceptionHandler(UserWithOrdersAssociatedException.class)
    public ResponseEntity<RestErrorMessage> handleUserWithOrdersAssociatedException(UserWithOrdersAssociatedException ex, HttpServletRequest request) {
        var errorStatus = HttpStatus.CONFLICT;
        var restErrorMessage = RestErrorMessage
                .builder()
                .title(errorStatus.getReasonPhrase())
                .status(errorStatus.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();
        return ResponseEntity
                .status(errorStatus)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(restErrorMessage);
    }


}
