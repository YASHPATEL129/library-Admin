package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Book;
import com.libraryAdmin.entity.Category;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.BookUpdateParam;
import com.libraryAdmin.model.params.UploadBookParam;
import com.libraryAdmin.pojo.response.BookUpdateResponse;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class BookServiceController {

    @Autowired
    private BookService bookService;


    @PostMapping("/createBook")
    public ResponseEntity<Success<?>> uploadBook(@RequestBody UploadBookParam param,
                                             BindingResult error,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new ValidationException(error);
        }
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(bookService.uploadBook(param));
        success.setMessage(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Success<?>> getBookById(@PathVariable Long id) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(bookService.getBook(id));
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @DeleteMapping("/isDeleted/{id}")
    public ResponseEntity<Success<?>> deleteBookById(@PathVariable Long id){
        bookService.deleteBook(id);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Success<?>> restoreBookById(@PathVariable Long id){
        bookService.restoreBook(id);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping("/all/books")
    public ResponseEntity<Success<?>> getAllBook() {
        List<Book> books = bookService.getAllBooks();
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(books);
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PutMapping("/update/books/{id}")
    public ResponseEntity<Success<?>> updateBook(@PathVariable Long id, @RequestBody(required = false) BookUpdateParam response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(bookService.updateBook(id, response));
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }


}