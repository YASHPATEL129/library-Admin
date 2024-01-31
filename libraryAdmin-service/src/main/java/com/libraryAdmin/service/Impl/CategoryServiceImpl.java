package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Category;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.CreateCategoryParam;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.CreateCategoryResponse;
import com.libraryAdmin.pojo.response.Error;
import com.libraryAdmin.repository.CategoryRepository;
import com.libraryAdmin.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentSession currentSession;

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryParam param, HttpServletRequest request, HttpServletResponse response) {
        String categoryName = param.getCategoryName();
        boolean exist = categoryRepository.existsByCategoryName(categoryName);
        if (exist){
            throw new ValidationException(Message.SERVER_ERROR , ErrorKeys.SERVER_ERROR);
        }
        Category category = new Category();
        category.setCategoryName(param.getCategoryName());
        category.setCreatedBy(currentSession.getUserName());
        categoryRepository.save(category);

        CreateCategoryResponse createCategoryResponse = new CreateCategoryResponse();
        createCategoryResponse.setId(category.getId());
        createCategoryResponse.setCategoryName(category.getCategoryName());
        return createCategoryResponse;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
