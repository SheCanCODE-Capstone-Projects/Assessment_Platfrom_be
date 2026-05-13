package com.talentprobe.assessment.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalEmailExceptionHandler {

    @ExceptionHandler(EmailDeliveryException.class)
    public ResponseEntity<EmailResponse> handleEmailException(EmailDeliveryException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new EmailResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmailResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Validation failed");
        return ResponseEntity.badRequest().body(new EmailResponse(false, msg));
    }
}