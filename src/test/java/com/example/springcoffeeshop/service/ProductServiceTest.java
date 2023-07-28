package com.example.springcoffeeshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.example.springcoffeeshop.dto.ProductRequestDTO;
import com.example.springcoffeeshop.model.Product;
import com.example.springcoffeeshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    Product product;

    @BeforeEach
    public void setup() {
        product = new Product(
                UUID.randomUUID(),
                "Product 1",
                "Description 1",
                5000,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                false);
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to verify if the product attributes are equal
    public void assertProductAttributesEqual(Product expected, Product actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPriceInCents(), actual.getPriceInCents());
        assertEquals(expected.getIsAvailable(), actual.getIsAvailable());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getDeletedAt(), actual.getDeletedAt());
        assertEquals(expected.getIsDeleted(), actual.getIsDeleted());
    }

    // Test cases for listAllProduct
    @Test
    public void shouldListAllProductsWithSucess() {
        when(repository.findByIsDeletedFalse(Sort.by(Sort.Direction.ASC, "name")))
            .thenReturn(List.of(product));

        List<Product> result = service.listAllProducts();

        assertNotNull(result);
        assertEquals(List.of(product), result);
        verify(repository).findByIsDeletedFalse(Sort.by(Sort.Direction.ASC, "name"));
        verifyNoMoreInteractions(repository);
    }

    // Test cases for findProductByID
    @Test
    public void shouldFindProductByIDWithSuccess() {
        when(repository.findById(product.getId()))
            .thenReturn(Optional.of(product));
        
        Product result = service.findProductByID(product.getId());

        assertNotNull(result);
        assertProductAttributesEqual(product, result);
        verify(repository).findById(product.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldReturnNullWhenProductNotFoundByID() {
        var nonExistentProductId = UUID.randomUUID();
        when(repository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        Product result = service.findProductByID(nonExistentProductId);

        assertNull(result);
        verify(repository).findById(nonExistentProductId);
        verifyNoMoreInteractions(repository);
    }

    // Test cases for createNewProduct
    @Test
    public void shouldCreateNewProductWithSuccess() {
        var request = new ProductRequestDTO(
                "Product Test",
                "Description Test",
                10000,
                true);

        var newProduct = new Product(request);

        when(repository.save(newProduct)).thenReturn(newProduct);

        Product result = service.createNewProduct(request);

        assertNotNull(result);
        assertProductAttributesEqual(newProduct, result);
        verify(repository).save(newProduct);
        verifyNoMoreInteractions(repository);
    }

    // Test cases for updateProductByID
    @Test
    public void shouldUpdateProductByIDWithSuccess() {
        var request = new ProductRequestDTO(
                "Updated Product Test",
                "Updated Description Test",
                11111,
                true);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPriceInCents(request.priceInCents());
        product.setIsAvailable(request.isAvailable());
        product.setUpdatedAt(LocalDateTime.now());

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        Product result = service.updateProductByID(product.getId(), request);

        assertNotNull(result);
        assertProductAttributesEqual(product, result);
        verify(repository).save(product);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldReturnNullWhenProductNotFoundInUpdateProduct() {
        UUID nonExistentProductId = UUID.randomUUID();
        var request = new ProductRequestDTO(
                "Updated Product Test 2",
                "Updated Description Test 2",
                22222,
                true);
        when(repository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        Product result = service.updateProductByID(nonExistentProductId, request);

        assertNull(result);
        verify(repository).findById(nonExistentProductId);
        verifyNoMoreInteractions(repository);
    }

    // Test cases for removeProductByID
    @Test
    public void shoulRemoveProductByIDWithSuccess() {
        when(repository.findById(product.getId()))
            .thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        Product result = service.removeProductByID(product.getId());

        assertNotNull(result);
        assertEquals(product.getDeletedAt(), result.getDeletedAt());
        assertTrue(result.getIsDeleted());
        verify(repository).findById(product.getId());
        verify(repository).save(product);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldReturnNullWhenProductNotFoundInRemoveProduct() {
        UUID nonExistentProductId = UUID.randomUUID();
        when(repository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        Product result = service.removeProductByID(nonExistentProductId);

        assertNull(result);
        verify(repository).findById(nonExistentProductId);
        verifyNoMoreInteractions(repository);
    }
}
