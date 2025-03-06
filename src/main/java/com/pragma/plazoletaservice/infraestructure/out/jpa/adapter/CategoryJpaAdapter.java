package com.pragma.plazoletaservice.infraestructure.out.jpa.adapter;

import com.pragma.plazoletaservice.domain.model.Category;
import com.pragma.plazoletaservice.domain.spi.ICategoryPersistencePort;
import com.pragma.plazoletaservice.infraestructure.exception.NoDataFoundException;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.CategoryEntity;
import com.pragma.plazoletaservice.infraestructure.out.jpa.mapper.ICategoryEntityMapper;
import com.pragma.plazoletaservice.infraestructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    
    @Override
    public Category saveCategory(Category category) {
        CategoryEntity categoryEntity = categoryRepository.save(categoryEntityMapper.toEntity(category));
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    public Category findCategoryById(Long id) {
        Optional<CategoryEntity> categoryEntityOptional= categoryRepository.findById(id);
        CategoryEntity categoryEntity = categoryEntityOptional.orElse(null);
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    public List<Category> findAllCategories() {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        if(categoryEntityList.isEmpty()){
            throw new NoDataFoundException();
        }
        return categoryEntityMapper.toCategoryList(categoryEntityList);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
