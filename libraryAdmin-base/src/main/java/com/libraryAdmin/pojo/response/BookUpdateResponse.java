package com.libraryAdmin.pojo.response;

import lombok.Data;

@Data
public class BookUpdateResponse {
    private Long id;
    private String title;
    private String description;
    private String isbn;
    private String publisher;
    private String author;
    private Long category;
    private Integer pages;
    private String attachmentNewFilename;
    private String adminImageNewImageName;
}
