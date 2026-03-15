package com.devsu.commons.exceptions;

import com.devsu.commons.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{
    private final HttpStatus status;
    private final String code;

    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
    }

    public AppException(ErrorCode errorCode, String message){
        super(message);
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
    }
}
