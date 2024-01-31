package com.libraryAdmin.service;

import com.libraryAdmin.entity.Category;
import com.libraryAdmin.model.params.CreateCategoryParam;
import com.libraryAdmin.pojo.response.CreateCategoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryParam param, HttpServletRequest request, HttpServletResponse response);

    List<Category> getAllCategories();
}
