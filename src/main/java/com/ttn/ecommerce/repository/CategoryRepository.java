package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(Category category);

    Optional<Category> findById(Long id);

    boolean existsByName(String name);

    List<Category> findByParentCategoryId_Id(Long categoryId);

    List<Category> findByParentCategory(Category parent);

    Category findByName(String category);

    Category findByNameAndParentCategoryIsNull(String name);

    List<Category> findAllByParentCategoryIsNull();

    List<Category> findAllByChildCategoriesIsNull();
}
