package com.libraryAdmin.model.params;

import lombok.Data;

@Data
public class BookUpdateParam {

    private String title;
    private String description;
    private String isbn;
    private String publisher;
    private String author;
    private String category;
    private Integer pages;
}
