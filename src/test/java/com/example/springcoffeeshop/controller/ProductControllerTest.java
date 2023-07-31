package com.example.springcoffeeshop.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.springcoffeeshop.dto.ProductRequestDTO;
import com.example.springcoffeeshop.model.Product;
import com.example.springcoffeeshop.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    private MockMvc mockMvc;

    private Product product;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        product = new Product(
                UUID.randomUUID(),
                "Product 1",
                "Description 1",
                10000,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                false);

        MockitoAnnotations.openMocks(this);
    }

    public String convertToJSON(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        return mapper.writeValueAsString(obj);
    }

    @Test
    public void shouldReturnAListOfProductOnGetProduct() throws Exception {
        List<Product> mockProductList = List.of(product);
        when(service.listAllProducts()).thenReturn(mockProductList);

        mockMvc.perform(get("/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(convertToJSON(mockProductList)));

        verify(service).listAllProducts();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldReturnAProductOnGetProductByID() throws Exception {
        when(service.findProductByID(product.getId())).thenReturn(product);

        mockMvc.perform(get("/product/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(convertToJSON(product)));

        verify(service).findProductByID(product.getId());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldReturnNotFoundWhenProductIDDoesNotExist() throws Exception {
        var nonExistentProductID = UUID.randomUUID();
        when(service.findProductByID(nonExistentProductID)).thenReturn(null);

        mockMvc.perform(get("/product/" + nonExistentProductID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(service).findProductByID(nonExistentProductID);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldCreateProductOnValidPostRequest() throws Exception {
        var request = new ProductRequestDTO(
                "Product Test",
                "Description Test",
                12222,
                true);
        var newProduct = new Product(request);
        when(service.createNewProduct(request)).thenReturn(newProduct);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJSON(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/product/" + newProduct.getId()))
                .andExpect(MockMvcResultMatchers.content().json(convertToJSON(newProduct)));

        verify(service).createNewProduct(request);
        verifyNoMoreInteractions(service);
    }

    // TODO: Create Unit Test for POST Request
    @Test
    public void shouldReturnBadRequestOnInvalidPostRequest() throws Exception {
    }
}
