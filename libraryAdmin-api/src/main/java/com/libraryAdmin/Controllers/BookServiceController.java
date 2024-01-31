package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.UploadBookParam;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        success.setMessage(Message.SUCCESS);
        success.setMessage(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Success<?>> getBookById(@PathVariable Long id) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(bookService.getBook(id));
        success.setMessage(Message.SUCCESS);
        success.setMessage(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }
}