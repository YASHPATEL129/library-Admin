package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {

    boolean existsByCategoryName(String categoryName);

    Category findByCategoryName(String category);
}
