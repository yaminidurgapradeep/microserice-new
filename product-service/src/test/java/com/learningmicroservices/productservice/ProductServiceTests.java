package com.learningmicroservices.productservice;

import com.learningmicroservices.productservice.dto.ProductRequest;
import com.learningmicroservices.productservice.dto.ProductResponse;
import com.learningmicroservices.productservice.model.Product;
import com.learningmicroservices.productservice.repository.ProductRepository;
import com.learningmicroservices.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void createProductTest() {
        ProductRequest productRequest = new ProductRequest("Product 1", "Description", 100.0);
        Product product = new Product("1", "Product 1", "Description", 100.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.createProduct(productRequest);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getAllProductsTest() {
        List<Product> products = List.of(
                new Product("1", "Product 1", "Description", 100.0)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> productResponses = productService.getAllProducts();

        assertEquals(1, productResponses.size());
        assertEquals("Product 1", productResponses.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }
}
