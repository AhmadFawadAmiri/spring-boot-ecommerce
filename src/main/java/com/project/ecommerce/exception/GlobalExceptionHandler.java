package com.project.ecommerce.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request){
    //public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Invalid input data",
                request.getRequestURI(),
                errors
        );
//        Map<String, Object> body = new HashMap<>();
//
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        body.put("error", "Validation Error");
//        body.put("errors", errors);
//        body.put("path", request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request){

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    //public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request){
//        Map<String, Object> body = new HashMap<>();
//
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        body.put("erorr", "Not found");
//        body.put("message", ex.getMessage());
//        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request){

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
//    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex, HttpServletRequest request){
//        Map<String, Object> body = new HashMap<>();
//
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        body.put("error", "Internal Server Error");
//        body.put("message", ex.getMessage());
//        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
