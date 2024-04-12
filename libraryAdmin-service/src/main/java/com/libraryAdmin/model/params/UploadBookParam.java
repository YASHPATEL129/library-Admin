package com.libraryAdmin.model.params;

import lombok.Data;

@Data
public class UploadBookParam {

    private String title;

    private String description;

    private Integer pages;

    private String isbn;

    private String publisher;

    private String author;

    private Long category;

    private String newImageName;

    private String newFilename;

    private Boolean isPrime;

}
