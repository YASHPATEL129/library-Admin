package com.libraryAdmin.service;

import com.libraryAdmin.entity.Book;
import com.libraryAdmin.interfaceProjections.BookProjection;
import com.libraryAdmin.model.params.BookUpdateParam;
import com.libraryAdmin.model.params.UploadBookParam;
import com.libraryAdmin.pojo.response.BookResponse;
import com.libraryAdmin.pojo.response.BookUpdateResponse;
import com.libraryAdmin.pojo.response.UploadBookResponse;

import java.util.List;

public interface BookService {

    UploadBookResponse uploadBook(UploadBookParam param);

    BookResponse getBook(Long id);

    void deleteBook(Long id);

    void restoreBook(Long id);

    List<BookProjection> getAllBooks();

    BookUpdateResponse updateBook(Long id , BookUpdateParam param);

    List<BookProjection> getSameBooks();

    List<BookProjection> searchBook(String query, String categoryIds);
}
