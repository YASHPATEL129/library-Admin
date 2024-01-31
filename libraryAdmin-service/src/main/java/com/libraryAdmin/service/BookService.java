package com.libraryAdmin.service;

import com.libraryAdmin.model.params.UploadBookParam;
import com.libraryAdmin.pojo.response.BookResponse;
import com.libraryAdmin.pojo.response.UploadBookResponse;

public interface BookService {

    UploadBookResponse uploadBook(UploadBookParam param);

    BookResponse getBook(Long id);
}
