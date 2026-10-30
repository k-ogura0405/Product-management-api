package com.example.producmanagementapi.service;

import com.example.producmanagementapi.dto.Product;
import com.example.producmanagementapi.exception.ProductNotFoundException;
import com.example.producmanagementapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    @Transactional
    public Product save(Product product) {
        productRepository.insert(product);
        return product;
    }

    @Transactional
    public Product update(Long id, Product product) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct == null) {
            throw new ProductNotFoundException(id);
        }

        product.setId(id);
        product.setCreatedAt(existingProduct.getCreatedAt());
        return productRepository.update(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.delete(id)) {
            throw new ProductNotFoundException(id);
        }
    }
}
