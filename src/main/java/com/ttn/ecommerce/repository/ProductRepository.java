package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Category;
import com.ttn.ecommerce.model.Product;
import com.ttn.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBySellerAndId(Seller seller, Long id);

    List<Product> findByCategory(Category category);

    Product findByIdAndIsDeletedAndIsActive(Long id, boolean b, boolean b1);

    Optional<Product> findByIdAndSeller(Long productId, Seller seller);

    boolean existsByCategoryId(Long parentId);

    List<Product> findBySellerAndIsDeletedFalse(Seller seller);
}
