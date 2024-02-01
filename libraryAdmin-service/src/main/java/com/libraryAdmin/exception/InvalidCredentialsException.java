package com.libraryAdmin.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidCredentialsException extends RuntimeException {

    String error;
    Object[] args;

    public InvalidCredentialsException(){
    }

    public InvalidCredentialsException(String messageKey){
        super(messageKey);
    }

    public InvalidCredentialsException(String message, String error){
        super(message);
        this.error = error;
    }

    public InvalidCredentialsException(String message, String error, Object[] args){
        super(message);
        this.error = error;
        this.args = args;
    }
}
