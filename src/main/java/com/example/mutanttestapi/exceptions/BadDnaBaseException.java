package com.example.mutanttestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadDnaBaseException extends ResponseStatusException {
    public BadDnaBaseException() {
        super(HttpStatus.BAD_REQUEST, "MT-02 -> The sequence should contain only [T,C,A,G] letters");
    }
}
