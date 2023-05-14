package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Category;
import com.ttn.ecommerce.model.Product;
import com.ttn.ecommerce.model.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBySellerAndId(Seller seller, Long id);

    List<Product> findByCategory(Category category);

    Product findByIdAndIsDeletedFalseAndIsActiveIsTrue(Long id);

    Optional<Product> findByIdAndSeller(Long productId, Seller seller);

    boolean existsByCategoryId(Long parentId);

    List<Product> findBySellerAndIsDeletedFalse(Seller seller);

    List<Product> findAllByCategory(Category category, Pageable pageable);

    List<Product> findAllBySeller(Seller seller, Pageable pageable);

    Optional<Product> findByNameAndSeller(String name, Seller seller);

    Optional<Product> findByNameAndCategoryAndSeller(String name, Long category, Seller seller);
}
