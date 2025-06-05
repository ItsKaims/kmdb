package com.example.movies.API.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPaginationException extends ResponseStatusException {
    public InvalidPaginationException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
