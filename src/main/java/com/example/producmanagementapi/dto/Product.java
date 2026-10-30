package com.example.producmanagementapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;

    @NotBlank(message = "商品名は必須です")
    private String name;

    @NotNull(message = "価格は必須です")
    @Min(value = 0, message = "価格は0以上である必要があります")
    private Integer price;

    @NotNull(message = "在庫数は必須です")
    @Min(value = 0, message = "在庫数は0以上である必要があります")
    private Integer stock;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
