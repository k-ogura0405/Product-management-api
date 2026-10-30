package com.example.producmanagementapi.repository;

import com.example.producmanagementapi.dto.Product;
import com.example.producmanagementapi.dto.PagedResult;
import com.example.producmanagementapi.exception.ProductNotFoundException;
import com.example.producmanagementapi.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final ProductMapper productMapper;

    public ProductRepository(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<Product> findAll() {
        return productMapper.findAll();
    }

    public Product findById(Long id) {
        return productMapper.findById(id);
    }

    public int insert(Product product) {
        return productMapper.insert(product);
    }

    public Product update(Product product) {
        if (productMapper.update(product) == 0) {
            throw new ProductNotFoundException(product.getId());
        }
        return product;
    }

    public boolean delete(Long id) {
        return productMapper.delete(id) == 1;
    }

    // ----- Pagination & filter -----
    public List<Product> findByCriteria(String keyword, Integer minPrice, Integer maxPrice, int offset, int limit) {
        return productMapper.findByCriteria(keyword, minPrice, maxPrice, offset, limit);
    }

    public long countByCriteria(String keyword, Integer minPrice, Integer maxPrice) {
        return productMapper.countByCriteria(keyword, minPrice, maxPrice);
    }
}

