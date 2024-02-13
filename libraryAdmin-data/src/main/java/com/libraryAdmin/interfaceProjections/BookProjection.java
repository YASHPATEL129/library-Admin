package com.libraryAdmin.interfaceProjections;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.libraryAdmin.entity.Book;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.time.LocalDateTime;

@Projection(name = "books", types = { Book.class })
public interface BookProjection {


    Long getBookId();
    String getTitle();
    String getCover();
    String getFile();
    String getDescription();
    Integer getPages();
    String getIsbn();
    String getPublisher();
    String getAuthor();
    Long getCategory();
    String getCreated_by();
    String getModified_by();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss.ss",timezone = "IST")
    LocalDateTime getCreated_date();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime getModified_date();
    Boolean getIs_deleted();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime getDeleted_date();

}
