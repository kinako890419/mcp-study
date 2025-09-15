package com.example.exercise.demo.exception;

import java.io.Serial;

public class DeleteFailException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public DeleteFailException(String message) {
        super(message);
    }
}
