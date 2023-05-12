package com.ttn.ecommerce.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GenericException extends RuntimeException {

    private HttpStatus httpStatus;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
