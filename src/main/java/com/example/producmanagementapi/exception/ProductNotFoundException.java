package com.example.producmanagementapi.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("指定されたIDの商品が見つかりません: " + id);
    }
}
