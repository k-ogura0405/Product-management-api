package com.example.producmanagementapi.mapper;

import com.example.producmanagementapi.dto.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT id, name, price, stock, description, created_at, updated_at FROM products ORDER BY id DESC")
    List<Product> findAll();

    @Select("SELECT id, name, price, stock, description, created_at, updated_at FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Insert("INSERT INTO products (name, price, stock, description) VALUES (#{name}, #{price}, #{stock}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id,createdAt,updatedAt", keyColumn = "id,created_at,updated_at")
    int insert(Product product);

    @Update("UPDATE products SET name = #{name}, price = #{price}, stock = #{stock}, description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int update(Product product);

    @Delete("DELETE FROM products WHERE id = #{id}")
    int delete(Long id);
}
