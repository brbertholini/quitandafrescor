package com.example.quitandafrescor.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductRepository productRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public void saveProduct(@RequestBody ProductRequestDTO data) {
        Product productData = new Product(data);

        productRepository.save(productData);
        return;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        List<ProductResponseDTO> productList = productRepository.findAll().stream().map(ProductResponseDTO::new)
                .toList();
        return productList;
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
    public void deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        productRepository.delete(optionalProduct.get());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO data) {
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
