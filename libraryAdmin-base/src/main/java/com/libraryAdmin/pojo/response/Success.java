package com.libraryAdmin.pojo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class Success<T> extends ResponseData<T> {

    private String message;

    @JsonSerialize
    private Object data;

    @JsonIgnore
    private String messageCode;
}
