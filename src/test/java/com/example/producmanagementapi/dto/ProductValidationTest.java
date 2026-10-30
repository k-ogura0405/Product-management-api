package com.example.producmanagementapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenProductValid_thenNoViolations() {
        Product product = new Product();
        product.setName("ノートPC");
        product.setPrice(120000);
        product.setStock(10);
        product.setDescription("高性能ノートPC");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameBlank_thenViolation() {
        Product product = new Product();
        product.setName("");
        product.setPrice(100);
        product.setStock(5);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void whenPriceNegative_thenViolation() {
        Product product = new Product();
        product.setName("マウス");
        product.setPrice(-1);
        product.setStock(5);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }

    @Test
    void whenStockNegative_thenViolation() {
        Product product = new Product();
        product.setName("マウス");
        product.setPrice(1000);
        product.setStock(-5);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("stock")));
    }
}
