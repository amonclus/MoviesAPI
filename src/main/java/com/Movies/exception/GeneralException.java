package com.Movies.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {
    public GeneralException(String message, HttpStatus notFound) {
        super(message);
    }

    private HttpStatus status;
}
