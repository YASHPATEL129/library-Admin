package com.libraryAdmin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BulkUpload {

    private String title;

    private String description;

    private String isbn;

    private String publisher;

    private String author;

    private String category;

    private Integer pages;

    private Boolean isPrime;

    private String originalFilename;

    private String originalImageName;
}
