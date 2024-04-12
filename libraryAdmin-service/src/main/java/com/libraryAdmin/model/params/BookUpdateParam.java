package com.libraryAdmin.model.params;

import lombok.Data;

@Data
public class BookUpdateParam {

     String title;
     String description;
     String isbn;
     String publisher;
     String author;
     Long category;
     Integer pages;
}
