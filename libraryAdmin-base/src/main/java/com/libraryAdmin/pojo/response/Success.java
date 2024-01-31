package com.libraryAdmin.pojo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Success<T> extends ResponseData<T> {

    private String message;

    private Object data;

    @JsonIgnore
    private String messageCode;
}
