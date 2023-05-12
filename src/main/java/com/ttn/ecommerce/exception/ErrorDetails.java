package com.ttn.ecommerce.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
