package com.pragma.plazoletaservice.domain.spi;

import com.pragma.plazoletaservice.domain.model.Category;

import java.util.List;

public interface ICategoryPersistencePort {
    Category saveCategory(Category category);

    Category findCategoryById(Long id);

    List<Category> findAllCategories();

    void deleteCategory(Long id);
}
