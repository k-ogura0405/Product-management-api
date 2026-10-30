package com.example.producmanagementapi.service;

import com.example.producmanagementapi.dto.Product;
import com.example.producmanagementapi.exception.ProductNotFoundException;
import com.example.producmanagementapi.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("ディスプレイ");
        sampleProduct.setPrice(35000);
        sampleProduct.setStock(15);
    }

    @Test
    void findAll_returnsAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> products = productService.findAll();
        assertEquals(1, products.size());
        assertEquals("ディスプレイ", products.get(0).getName());
    }

    @Test
    void findById_whenFound_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(sampleProduct);

        Product result = productService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_whenNotFound_throwsException() {
        when(productRepository.findById(1L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void save_insertsAndReturnsProduct() {
        Product newProduct = new Product();
        newProduct.setName("USBハブ");
        newProduct.setPrice(2000);
        newProduct.setStock(50);

        when(productRepository.insert(newProduct)).thenReturn(1);

        Product saved = productService.save(newProduct);
        assertEquals(newProduct, saved);
        verify(productRepository, times(1)).insert(newProduct);
    }

    @Test
    void update_whenFound_updatesAndReturns() {
        Product updateData = new Product();
        updateData.setName("ディスプレイ(改)");
        updateData.setPrice(38000);
        updateData.setStock(12);

        when(productRepository.findById(1L)).thenReturn(sampleProduct);
        when(productRepository.update(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = productService.update(1L, updateData);
        assertEquals(1L, updated.getId());
        assertEquals("ディスプレイ(改)", updated.getName());
    }

    @Test
    void update_whenNotFound_throwsException() {
        Product updateData = new Product();
        when(productRepository.findById(1L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.update(1L, updateData));
    }

    @Test
    void delete_whenFound_deletesSuccessfully() {
        when(productRepository.delete(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productService.delete(1L));
        verify(productRepository, times(1)).delete(1L);
    }

    @Test
    void delete_whenNotFound_throwsException() {
        when(productRepository.delete(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));
    }
}
