package com.libraryAdmin.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookResponse {

    private Long bookId;
    private String title;
    private String cover;
    private String file;
    private String description;
    private Integer pages;
    private String isbn;
    private String publisher;
    private String author;
    private Long category;
    private String createdBy;
    private String modifiedBy;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss.ss",timezone = "IST")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime modifiedDate;
    private Boolean IsDeleted;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedDate;
}
