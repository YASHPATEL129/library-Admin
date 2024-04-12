package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Category;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.CreateCategoryParam;
import com.libraryAdmin.model.params.UpdateCategoryParam;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CategoryServiceController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/createCategory")
    public ResponseEntity<Success<?>> signIn(@RequestBody @Validated CreateCategoryParam createCategoryParam,
                                             BindingResult error,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new ValidationException(error);
        }
        Object data = categoryService.createCategory(createCategoryParam, request, response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(data);
        success.setMessageCode(Message.CATEGORY_CREATE_SUCCESSFUL);
        return respBuilder.body(success);
       }

    @GetMapping("/all/category")
    public ResponseEntity<Success<?>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(categories);
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @DeleteMapping("/delete/category/{id}")
    public ResponseEntity<Success<?>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.CATEGORY_DELETE_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PutMapping("/update/category/{id}")
    public ResponseEntity<Success<?>> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryParam param){
        categoryService.updateCategory(id, param);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.CATEGORY_UPDATE_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping("/get/category/{id}")
    public ResponseEntity<Success<?>> getCategories(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(category);
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }
}
