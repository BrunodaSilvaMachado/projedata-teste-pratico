package br.com.java.autoflex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.ProductMaterialRequestDTO;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;
import br.com.java.autoflex.service.ProductService;

public class ProductControllerTest {
    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    public void shouldCreateProductSuccessfully() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car");
        requestDTO.setPrice(new BigDecimal("1000"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        when(productService.create(requestDTO)).thenReturn(new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>()));

        ProductResponseDTO responseDTO = productController.create(requestDTO);
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
    }

    @Test
    public void shouldFindAllProductsSuccessfully() {
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());
        when(productService.findAll()).thenReturn(new ArrayList<>(List.of(responseDTO)));

        List<ProductResponseDTO> products = productController.findAll();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void shouldFindByIdSuccessfully() {
        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());
        when(productService.findById(1L)).thenReturn(responseDTO);

        ProductResponseDTO result = productController.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void shouldUpdateProductWithMaterials() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car");
        requestDTO.setPrice(new BigDecimal("1000"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        ProductResponseDTO responseDTO = new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());
        when(productService.update(1L, requestDTO)).thenReturn(responseDTO);

        ProductResponseDTO result = productController.update(1L, requestDTO);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldDeleteSuccessfully() {
        productController.delete(1L);
        verify(productService).delete(1L);
    }
}
