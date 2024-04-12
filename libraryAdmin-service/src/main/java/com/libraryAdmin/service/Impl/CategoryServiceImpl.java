package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Book;
import com.libraryAdmin.entity.Category;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.NotFoundException;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.CreateCategoryParam;
import com.libraryAdmin.model.params.UpdateCategoryParam;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.CreateCategoryResponse;
import com.libraryAdmin.pojo.response.DeleteCategoryResponse;
import com.libraryAdmin.pojo.response.Error;
import com.libraryAdmin.repository.BookRepository;
import com.libraryAdmin.repository.CategoryRepository;
import com.libraryAdmin.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryParam param, HttpServletRequest request, HttpServletResponse response) {
        String categoryName = param.getCategoryName();
        boolean exist = categoryRepository.existsByCategoryName(categoryName);
        if (exist){
            throw new ValidationException( Message.CATEGORY_IS_ALREADY_EXIST,ErrorKeys.CATEGORY_IS_ALREADY_EXIST );
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

    @Override
    public void deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // Check if the categoryId is associated with any books
            List<Book> books = bookRepository.findByCategory(id);

            if (!books.isEmpty()) {
                // The categoryId is associated with books, cannot delete
             throw new InvalidCredentialsException(Message.CATEGORY_ID_ASSOCIATED_WITH_BOOK, ErrorKeys.CATEGORY_ID_ASSOCIATED_WITH_BOOK);
            }

            // Delete the category since it is not associated with any books
            categoryRepository.delete(category);

        } else {
            // Category with the given categoryId does not exist
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
    }

    @Override
    public void updateCategory(Long id, UpdateCategoryParam param) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();

            // If categoryName is provided in the requestDTO, check if it already exists in the database
            String newCategoryName = param.getCategoryName();
            if (newCategoryName != null && !newCategoryName.isEmpty()) {
                boolean isCategoryNameExists = categoryRepository.existsByCategoryName(newCategoryName);

                if (isCategoryNameExists) {
                    // The categoryName provided in the request already exists, show the error
                    throw new InvalidCredentialsException(Message.IS_ALREADY_EXIST, ErrorKeys.IS_ALREADY_EXIST);
                }

                // Update the categoryName in the existing category
                existingCategory.setCategoryName(newCategoryName);
                existingCategory.setModifiedBy(currentSession.getUserName());
            }

            // Save the updated category
            categoryRepository.save(existingCategory);
        } else {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
    }

    @Override
    public Category getCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            Category category = new Category();
            category.setId(id);
            category.setCategoryName(existingCategory.getCategoryName());
            category.setCreatedDate(existingCategory.getCreatedDate());
            category.setCreatedBy(existingCategory.getCreatedBy());
            category.setModifiedDate(existingCategory.getModifiedDate());
            category.setModifiedBy(existingCategory.getModifiedBy());
            return category;
        }else {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }

    }
}
