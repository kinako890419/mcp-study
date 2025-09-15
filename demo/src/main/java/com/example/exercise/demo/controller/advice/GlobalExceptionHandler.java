package com.example.exercise.demo.controller.advice;

import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.enums.ResponseStatusEnums;
import com.example.exercise.demo.exception.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMsg> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(error.getDefaultMessage());
        }
        return new ResponseEntity<>(createFailResponse(sb.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseMsg> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>(createFailResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<ResponseMsg> handleDuplicatedDataException(DuplicatedDataException ex) {
        return new ResponseEntity<>(createFailResponse(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsertionFailException.class)
    public ResponseEntity<ResponseMsg> handleInsertionFailException(InsertionFailException ex) {
        return new ResponseEntity<>(createFailResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeleteFailException.class)
    public ResponseEntity<ResponseMsg> handleDeleteFailException(DeleteFailException ex) {
        return new ResponseEntity<>(createFailResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    // Hibernate
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseMsg> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(createFailResponse("Constraint violation: " + ex.getConstraintName()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseMsg> handleDataIntegrityViolationException(DataAccessException ex) {
        return new ResponseEntity<>(createFailResponse("DB Data integrity violation: " + ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMsg> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(createFailResponse("Unexpected error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseMsg createFailResponse(String message) {
        ResponseMsg resp = new ResponseMsg();
        resp.setStatus(ResponseStatusEnums.FAIL.getStatus());
        resp.setMessage(message);
        return resp;
    }

}
