package com.libraryAdmin.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    String error;

    public NotFoundException(){
    }

    public NotFoundException(String messageKey){
        super(messageKey);
    }

    public NotFoundException(String message, String error){
        super(message);
        this.error = error;
    }
}
