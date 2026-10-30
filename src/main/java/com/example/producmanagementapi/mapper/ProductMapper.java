package com.example.producmanagementapi.mapper;

import com.example.producmanagementapi.dto.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

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

    // ----- New methods for pagination, search, and price filter -----
    @Select({
        "<script>",
        "SELECT id, name, price, stock, description, created_at, updated_at FROM products",
        "WHERE 1=1",
        "<if test='keyword != null and keyword != \"\"'>",
        "  AND name LIKE CONCAT('%', #{keyword}, '%')",
        "</if>",
        "<if test='minPrice != null'>",
        "  AND price &gt;= #{minPrice}",
        "</if>",
        "<if test='maxPrice != null'>",
        "  AND price &lt;= #{maxPrice}",
        "</if>",
        "ORDER BY id DESC",
        "LIMIT #{limit} OFFSET #{offset}",
        "</script>"
    })
    List<Product> findByCriteria(@Param("keyword") String keyword,
                                 @Param("minPrice") Integer minPrice,
                                 @Param("maxPrice") Integer maxPrice,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    @Select({
        "<script>",
        "SELECT COUNT(*) FROM products",
        "WHERE 1=1",
        "<if test='keyword != null and keyword != \"\"'>",
        "  AND name LIKE CONCAT('%', #{keyword}, '%')",
        "</if>",
        "<if test='minPrice != null'>",
        "  AND price &gt;= #{minPrice}",
        "</if>",
        "<if test='maxPrice != null'>",
        "  AND price &lt;= #{maxPrice}",
        "</if>",
        "</script>"
    })
    int countByCriteria(@Param("keyword") String keyword,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice);
}
