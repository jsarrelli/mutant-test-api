package com.example.mutanttestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DnaNotAMatrixException extends ResponseStatusException {
    public DnaNotAMatrixException() {
        super(HttpStatus.BAD_REQUEST, "MT-01 -> DNA is not a NxN matrix");
    }
}
