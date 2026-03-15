package com.devsu.app.config;

import com.devsu.commons.dto.ErrorResponse;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("HTTP message not readable exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ErrorCode.HTTP_MSG_NOT_READABLE));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        String message = String.join(",",errors);
        log.error("Method argument not valid exception occurred: {}", message);
        return buildResponseEntity(new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID, message));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("Missing request header exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ErrorCode.MISSING_REQUEST_HEADER));
    }
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(NoResourceFoundException ex) {
        log.error("No resource found exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ErrorCode.NO_RESOURCE_FOUND));
    }

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<ErrorResponse> handleMethodAppException(AppException ex) {
        log.error("Application exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ex.getStatus(), ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleMethodBadException(DataAccessException ex) {
        log.error("Data access exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ErrorCode.DATA_ACCESS_EXCEPTION));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleMethodBadException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage());
        return buildResponseEntity(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(@NonNull ErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
