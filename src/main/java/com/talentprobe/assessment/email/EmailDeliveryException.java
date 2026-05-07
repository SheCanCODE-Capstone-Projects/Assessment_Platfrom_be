package com.talentprobe.assessment.email;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailDeliveryException extends RuntimeException {
    private final HttpStatus status;

    public EmailDeliveryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}