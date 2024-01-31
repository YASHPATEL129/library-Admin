package com.libraryAdmin.pojo.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadBookResponse {

    private String title;

    private String description;

    private String isbn;

    private String publisher;

    private String author;

    private Long category;

    private Integer pages;

    private String createdBy;

    private Boolean isPrime;
}
