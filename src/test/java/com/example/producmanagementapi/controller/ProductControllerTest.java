package com.example.producmanagementapi.controller;

import com.example.producmanagementapi.dto.Product;
import com.example.producmanagementapi.exception.ProductNotFoundException;
import com.example.producmanagementapi.exception.handler.GlobalExceptionHandler;
import com.example.producmanagementapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("有線マウス");
        sampleProduct.setPrice(1500);
        sampleProduct.setStock(30);
        sampleProduct.setDescription("エルゴノミクス有線マウス");
    }

    @Test
    void findAll_returns200AndList() throws Exception {
        when(productService.findAll()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("有線マウス")));
    }

    @Test
    void findById_whenFound_returns200AndProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("有線マウス")));
    }

    @Test
    void findById_whenNotFound_returns404() throws Exception {
        when(productService.findById(99L)).thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("指定されたIDの商品が見つかりません: 99")));
    }

    @Test
    void create_whenValid_returns201AndLocationHeader() throws Exception {
        when(productService.save(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"有線マウス\", \"price\": 1500, \"stock\": 30}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/products/1")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void create_whenInvalid_returns400WithErrors() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"price\": -100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", notNullValue()))
                .andExpect(jsonPath("$.errors.price", notNullValue()));
    }

    @Test
    void update_whenValid_returns200AndUpdatedProduct() throws Exception {
        when(productService.update(eq(1L), any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"有線マウス\", \"price\": 1800, \"stock\": 25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void update_whenNotFound_returns404() throws Exception {
        when(productService.update(eq(99L), any(Product.class))).thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(put("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"有線マウス\", \"price\": 1800, \"stock\": 25}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenFound_returns204() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_returns404() throws Exception {
        doThrow(new ProductNotFoundException(99L)).when(productService).delete(99L);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }
}
