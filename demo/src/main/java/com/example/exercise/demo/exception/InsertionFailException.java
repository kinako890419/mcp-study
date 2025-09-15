package com.example.exercise.demo.exception;

import java.io.Serial;

public class InsertionFailException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public InsertionFailException(String message) {
        super(message);
    }
}
