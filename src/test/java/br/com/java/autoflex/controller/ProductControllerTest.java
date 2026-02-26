package br.com.java.autoflex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;
import br.com.java.autoflex.fixture.ProductTestFixture;
import br.com.java.autoflex.fixture.TestConstants;
import br.com.java.autoflex.service.ProductService;

/**
 * Unit tests for ProductController.
 * Tests REST endpoints for product management.
 */
class ProductControllerTest {
    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    void testCreateProductSuccessfully() {
        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID, TestConstants.QUANTITY_50))));

        ProductResponseDTO responseDTO = ProductTestFixture.createProductResponse(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productService.create(requestDTO)).thenReturn(responseDTO);

        ProductResponseDTO result = productController.create(requestDTO);

        assertNotNull(result);
        assertEquals(TestConstants.PRODUCT_ID, result.getId());
    }

    @Test
    void testFindAllProductsSuccessfully() {
        ProductResponseDTO responseDTO = ProductTestFixture.createProductResponse(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productService.findAll()).thenReturn(new ArrayList<>(List.of(responseDTO)));

        List<ProductResponseDTO> products = productController.findAll();

        assertNotNull(products);
        assertEquals(1, products.size());
    }

    @Test
    void testFindByIdSuccessfully() {
        ProductResponseDTO responseDTO = ProductTestFixture.createProductResponse(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productService.findById(TestConstants.PRODUCT_ID)).thenReturn(responseDTO);

        ProductResponseDTO result = productController.findById(TestConstants.PRODUCT_ID);

        assertNotNull(result);
        assertEquals(TestConstants.PRODUCT_ID, result.getId());
    }

    @Test
    void testUpdateProductWithMaterialsSuccessfully() {
        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID, TestConstants.QUANTITY_50))));

        ProductResponseDTO responseDTO = ProductTestFixture.createProductResponse(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productService.update(TestConstants.PRODUCT_ID, requestDTO)).thenReturn(responseDTO);

        ProductResponseDTO result = productController.update(TestConstants.PRODUCT_ID, requestDTO);

        assertNotNull(result);
        assertEquals(TestConstants.PRODUCT_ID, result.getId());
    }

    @Test
    void testDeleteProductSuccessfully() {
        productController.delete(TestConstants.PRODUCT_ID);
        verify(productService).delete(TestConstants.PRODUCT_ID);
    }
}
