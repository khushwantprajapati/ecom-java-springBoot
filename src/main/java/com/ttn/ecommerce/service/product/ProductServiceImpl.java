package com.ttn.ecommerce.service.product;

import com.ttn.ecommerce.dto.ProductDto;
import com.ttn.ecommerce.dto.ProductResponseDto;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Category;
import com.ttn.ecommerce.model.Product;
import com.ttn.ecommerce.model.Seller;
import com.ttn.ecommerce.repository.*;
import com.ttn.ecommerce.service.EmailSenderService;
import com.ttn.ecommerce.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ProductServiceImpl implements ProductService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    @Override
    public ResponseEntity<?> addProduct(ProductDto productDto) {
        if (Objects.isNull(productDto)) { // Check if productDto is null
            throw new GenericException("Invalid input parameters", HttpStatus.BAD_REQUEST);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Seller seller = sellerRepository.findByEmailIgnoreCase(email); // Find seller by email

        if (Objects.isNull(seller)) { // Check if seller is null
            throw new GenericException("Invalid seller", HttpStatus.NOT_FOUND);
        }

        Product product = new Product();
        product.setBrand(productDto.getBrand());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSeller(seller);

        // name should be unique and category id pass should be valid leaf node

        Optional<Category> category = categoryRepository.findById(productDto.getCategory()); // Find category by ID
        if (category.isEmpty()) { // Check if category is empty
            return ResponseEntity.badRequest().body("Invalid category name"); // Return bad request response if category is empty
        }
        product.setCategory(category.get()); // Set category for product

        productAddedMail(product); // Send email to admin about newly added product
        productRepository.save(product); // Save product in repository

        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully"); // Return success response
    }


    public void productAddedMail(Product product) {
        String subject = "New Product Added: " + product.getName();
        String body = "Hello,\n\nA new product has been added and requires your attention.\n\n"
                + "Product Details:\n\n"
                + "Name: " + product.getName() + "\n"
                + "Brand: " + product.getBrand() + "\n"
                + "Category: " + product.getCategory().getName() + "\n"
                + "Description: " + product.getDescription() + "\n"
                + "Seller: " + product.getSeller().getFirstName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("Khushwant5265@gmail.com");
        message.setSubject(subject);
        message.setText(body);
        emailSenderService.sendEmail(message);
    }

    @Override
    public ResponseEntity<?> viewProduct(Long id) {
        // Validate token and retrieve email of the seller from the token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Retrieve the seller from the email
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        // Throw error if seller is not found
        if (Objects.isNull(seller)) {
            throw new GenericException("Invalid seller", HttpStatus.NOT_FOUND);
        }

        // Retrieve the product by ID and seller
        Product product = productRepository.findBySellerAndId(seller, id);

        // Throw error if product is not found or is deleted
        if (Objects.isNull(product) || product.getIsDeleted()) {
            throw new GenericException("Invalid product ID", HttpStatus.NOT_FOUND);
        }

        // Create a ProductResponseDto object and set its properties from the retrieved product
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setBrand(product.getBrand());
        productResponseDto.setIsCancellable(product.getIsCancellable());
        productResponseDto.setIsReturnable(product.getIsReturnable());
        productResponseDto.setIsActive(product.getIsActive());
        productResponseDto.setIsDeleted(product.getIsDeleted());

        // Return the ProductResponseDto object in the response body
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }


    @Override
    public ResponseEntity<List<ProductResponseDto>> viewAllProduct() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Retrieve seller object from database using the email
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        // Check if seller exists, throw exception if not
        if (Objects.isNull(seller)) {
            throw new GenericException("Invalid seller", HttpStatus.NOT_FOUND);
        }

        // Retrieve list of products created by the seller, excluding the deleted products
        List<Product> products = productRepository.findBySellerAndIsDeletedFalse(seller);
        // Convert the list of Product objects to a list of ProductResponseDto objects using stream and map
        List<ProductResponseDto> productResponseDtoList = products.stream()
                .map(product -> {
                    // Create a new ProductResponseDto object and set its properties from the Product object
                    ProductResponseDto productDto = new ProductResponseDto();
                    productDto.setId(product.getId());
                    productDto.setName(product.getName());
                    productDto.setDescription(product.getDescription());
                    productDto.setBrand(product.getBrand());
                    productDto.setIsCancellable(product.getIsCancellable());
                    productDto.setIsReturnable(product.getIsReturnable());
                    productDto.setIsActive(product.getIsActive());
                    productDto.setIsDeleted(product.getIsDeleted());
                    // Set category details for the product response DTO using a static method of CategoryDto class
//                    productDto.setCategoryDetails(CategoryDto.fromCategory(product.getCategory()));
                    // Return the ProductResponseDto object
                    return productDto;
                })
                .collect(Collectors.toList());

        // Return the list of ProductResponseDto objects in the response body with HTTP status code 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtoList);
    }


    @Override
    public ResponseEntity<?> deleteProduct( Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Retrieve the seller based on the email
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        // Retrieve the product by the ID and the seller
        Product product = productRepository.findBySellerAndId(seller, id);

        // Check if the product exists
        if (Objects.isNull(product)) {
            // If the product does not exist, return a 404 error response
            throw new GenericException("Product not found", HttpStatus.NOT_FOUND);
        }

        // Delete the product
        product.setIsDeleted(Boolean.TRUE);
        productRepository.save(product);

        // Return a success response
        return ResponseEntity.status(HttpStatus.OK).body("Product successfully deleted");
    }


    @Override
    public ResponseEntity<?> updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setDescription(productDto.getDescription());
        product.setIsReturnable(productDto.getIsReturnable());
        product.setIsCancellable(productDto.getIsCancellable());

        Optional<Category> category = categoryRepository.findById(productDto.getCategory());
        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid category name");
        }
        product.setCategory(category.get());

        productRepository.save(product);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> viewProductCustomer(Long id) {

        //Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        Product product = productRepository.findByIdAndIsDeletedAndIsActive(id, false, true);
        if (product == null) {
            throw new GenericException("Invalid product ID or product not available", HttpStatus.NOT_FOUND);
        }

//        List<ProductVariation> variations = productVariationRepository.findByProductAndIsDeleted(product, false);
//        if (variations == null || variations.isEmpty()) {
//            throw new GenericException("Product has no valid variations", HttpStatus.BAD_REQUEST);
//        }

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setBrand(product.getBrand());
        productResponseDto.setIsCancellable(product.getIsCancellable());
        productResponseDto.setIsReturnable(product.getIsReturnable());
        productResponseDto.setIsActive(product.getIsActive());
        productResponseDto.setIsDeleted(product.getIsDeleted());

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }


    @Override
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        List<Product> products = productRepository.findByCategory(category);
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product product : products) {
            ProductDto productDto = new ProductDto();

            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory().getId());
            productDtoList.add(productDto);
        }

        return ResponseEntity.ok().body(productDtoList);
    }


    public ResponseEntity<List<ProductDto>> getSimilarProducts(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Category category = product.getCategory();


        // Find other products in the same category and variation as the current product.
        List<Product> similarProducts = productRepository.findByCategory(category);

        // Convert the list of products to a list of product DTOs.
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product similarProduct : similarProducts) {
            ProductDto productDto = new ProductDto();
            productDto.setBrand(similarProduct.getBrand());
            productDto.setName(similarProduct.getName());
            productDto.setDescription(similarProduct.getDescription());
            productDto.setCategory(similarProduct.getCategory().getId());
//            productDto.setVariation(similarProduct.getVariation().getName());
            productDtoList.add(productDto);
        }


        return ResponseEntity.ok().body(productDtoList);
    }


    // admin
    @Override
    public ResponseEntity<?> getProductAdmin(Long id) {

        // Check if the product ID is valid
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid product ID");
        }

        // Retrieve the product details from the repository
        Product product = productRepository.findById(id).orElse(null);

        // Check if the product exists
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Create a ProductResponseDto containing the relevant product details
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(product.getId());
        responseDto.setName(product.getName());
        responseDto.setDescription(product.getDescription());
        responseDto.setBrand(product.getBrand());
        responseDto.setIsCancellable(product.getIsCancellable());
        responseDto.setIsReturnable(product.getIsReturnable());
        responseDto.setIsActive(product.getIsActive());
        responseDto.setIsDeleted(product.getIsDeleted());


        // Return the product details in the response
        return ResponseEntity.ok().body(responseDto);
    }


    @Override
    public ResponseEntity<List<ProductResponseDto>> viewAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : products) {
            ProductResponseDto responseDto = new ProductResponseDto();
            responseDto.setId(product.getId());
            responseDto.setName(product.getName());
            responseDto.setDescription(product.getDescription());
            responseDto.setBrand(product.getBrand());
            responseDto.setIsCancellable(product.getIsCancellable());
            responseDto.setIsReturnable(product.getIsReturnable());
            responseDto.setIsActive(product.getIsActive());
            responseDto.setIsDeleted(product.getIsDeleted());



            responseDtoList.add(responseDto);
        }

        return ResponseEntity.ok().body(responseDtoList);
    }


    @Override
    public ResponseEntity<?> deactivateProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (product.getIsDeleted() || !product.getIsActive()) {
            return ResponseEntity.badRequest().body("Product already inactive");
        }

        product.setIsActive(false);
        productRepository.save(product);

        return ResponseEntity.ok().body("Product deactivated successfully");
    }

    @Override
    public ResponseEntity<?> activateProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (product.getIsDeleted() || product.getIsActive()) {
            return ResponseEntity.badRequest().body("Product already active");
        }

        product.setIsActive(true);
        productRepository.save(product);

        return ResponseEntity.ok().body("Product activated successfully");
    }


}
