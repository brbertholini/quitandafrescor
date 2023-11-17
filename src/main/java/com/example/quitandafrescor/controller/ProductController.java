package com.example.quitandafrescor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.example.quitandafrescor.dto.ProductRequestDTO;
import com.example.quitandafrescor.dto.ProductResponseDTO;
import com.example.quitandafrescor.dto.ProductUpdateDTO;
import com.example.quitandafrescor.dto.ProductUpdateDTOReturn;
import com.example.quitandafrescor.model.Product;
import com.example.quitandafrescor.repository.ProductRepository;

@RestController
@RequestMapping("product")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/relatedProducts/{category}")
    public List<ProductResponseDTO> getRelatedProducts(@PathVariable String category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> saveProduct(@RequestBody ProductRequestDTO data) {
        Optional<Product> existingProductWithName = productRepository.findByNameIgnoreCase(data.name());
        Optional<Product> existingProductWithImage = productRepository.findByImage(data.image());
        if (existingProductWithName.isPresent()) {
            return new ResponseEntity<>("Product with the same name already exists", HttpStatus.BAD_REQUEST);
        } else if (existingProductWithImage.isPresent()) {
            return new ResponseEntity<>("Product with the same image URL already exists", HttpStatus.BAD_REQUEST);
        } else {
            Product productData = new Product(data);
            productRepository.save(productData);
            return new ResponseEntity<>("Product saved successfully", HttpStatus.CREATED);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productRepository.findAll().stream().map(ProductResponseDTO::new)
                .toList();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductResponseDTO productResponseDTO = new ProductResponseDTO(product);
            return ResponseEntity.ok(productResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductUpdateDTOReturn> updateProduct(@PathVariable Long id,
            @RequestBody ProductUpdateDTO data) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.updateProduct(data);
            productRepository.save(product);
            return ResponseEntity.ok(new ProductUpdateDTOReturn(product));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    

}
