package com.example.producmanagementapi.repository;

import com.example.producmanagementapi.dto.Product;
import com.example.producmanagementapi.exception.ProductNotFoundException;
import com.example.producmanagementapi.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepository productRepository;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("キーボード");
        sampleProduct.setPrice(5000);
        sampleProduct.setStock(20);
    }

    @Test
    void findAll_returnsList() {
        when(productMapper.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> result = productRepository.findAll();
        assertEquals(1, result.size());
        assertEquals("キーボード", result.get(0).getName());
        verify(productMapper, times(1)).findAll();
    }

    @Test
    void findById_returnsProduct() {
        when(productMapper.findById(1L)).thenReturn(sampleProduct);

        Product result = productRepository.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productMapper, times(1)).findById(1L);
    }

    @Test
    void insert_callsMapperInsert() {
        when(productMapper.insert(sampleProduct)).thenReturn(1);

        int result = productRepository.insert(sampleProduct);
        assertEquals(1, result);
        verify(productMapper, times(1)).insert(sampleProduct);
    }

    @Test
    void update_successReturnsProduct() {
        when(productMapper.update(sampleProduct)).thenReturn(1);

        Product result = productRepository.update(sampleProduct);
        assertEquals(sampleProduct, result);
        verify(productMapper, times(1)).update(sampleProduct);
    }

    @Test
    void update_notFoundThrowsException() {
        when(productMapper.update(sampleProduct)).thenReturn(0);

        assertThrows(ProductNotFoundException.class, () -> productRepository.update(sampleProduct));
    }

    @Test
    void delete_successReturnsTrue() {
        when(productMapper.delete(1L)).thenReturn(1);

        boolean result = productRepository.delete(1L);
        assertTrue(result);
        verify(productMapper, times(1)).delete(1L);
    }

    @Test
    void delete_failureReturnsFalse() {
        when(productMapper.delete(1L)).thenReturn(0);

        boolean result = productRepository.delete(1L);
        assertFalse(result);
        verify(productMapper, times(1)).delete(1L);
    }
}
