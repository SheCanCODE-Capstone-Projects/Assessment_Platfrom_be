package com.talentprobe.assessment.exception;

import com.talentprobe.assessment.enums.Language;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 - Duplicate record
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicate(DuplicateResourceException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400 - Invalid enum value.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleInvalidEnum(HttpMessageNotReadableException ex) {
        String allowed = Arrays.stream(Language.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        String msg = "Invalid programming language. Allowed: " + allowed;
        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }

    // 400 - @Valid validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Invalid input provided");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // 400 - Runtime business errors from services
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 409 DB constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rootMsg = ex.getMostSpecificCause().getMessage().toLowerCase();

        String friendlyMsg;
        if (rootMsg.contains("users_language_check") || rootMsg.contains("language")) {
            friendlyMsg = "Invalid programming language selected. Please choose from the available options.";
        } else if (rootMsg.contains("unique") || rootMsg.contains("duplicate")) {
            friendlyMsg = "A record with this information already exists.";
        } else if (rootMsg.contains("foreign key")) {
            friendlyMsg = "The referenced item does not exist.";
        } else if (rootMsg.contains("not null")) {
            friendlyMsg = "Required information is missing.";
        } else {
            friendlyMsg = "Unable to save data. Please verify your input and try again.";
        }

        return buildResponse(HttpStatus.CONFLICT, friendlyMsg);
    }

    // 500 - Everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ApiResponse> buildResponse(HttpStatus status, String message) {
        ApiResponse body = new ApiResponse(
                false,
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }

    public record ApiResponse(
            boolean success,
            int status,
            String error,
            String message,
            LocalDateTime timestamp
    ) {}
}