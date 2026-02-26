package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;
import br.com.java.autoflex.exception.BusinessException;
import br.com.java.autoflex.exception.ResourceNotFoundException;
import br.com.java.autoflex.fixture.ProductTestFixture;
import br.com.java.autoflex.fixture.RawMaterialTestFixture;
import br.com.java.autoflex.fixture.TestConstants;
import br.com.java.autoflex.mapper.ProductMapper;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;

/**
 * Unit tests for ProductServiceImpl.
 * Tests CRUD operations and business logic for product management.
 */
class ProductServiceImplTest {
    private ProductServiceImpl productService;
    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private RawMaterialRepository rawMaterialRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productMapper = mock(ProductMapper.class);
        rawMaterialRepository = mock(RawMaterialRepository.class);
        productService = new ProductServiceImpl(productRepository, productMapper, rawMaterialRepository);
    }

    @Test
    void testCreateProductSuccessfully() {
        RawMaterial rawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID, TestConstants.QUANTITY_50))));

        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);
        ProductMaterial pm = ProductTestFixture.createProductMaterial(1L, product, rawMaterial, TestConstants.QUANTITY_50);
        product.getMaterials().add(pm);

        when(productRepository.existsByCode(TestConstants.PRODUCT_CODE)).thenReturn(false);
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.of(rawMaterial));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDTO(product))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                        TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE));

        ProductResponseDTO response = productService.create(requestDTO);

        assertNotNull(response);
        assertEquals(TestConstants.PRODUCT_ID, response.getId());
    }

    @Test
    void testCreateProductWithDuplicateCodeThrowsException() {
        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productRepository.existsByCode(TestConstants.PRODUCT_CODE)).thenReturn(true);

        try {
            productService.create(requestDTO);
        } catch (BusinessException ex) {
            assertEquals(TestConstants.PRODUCT_CODE_ALREADY_EXISTS, ex.getMessage());
        }
    }

    @Test
    void testCreateProductWithNonExistentMaterialThrowsException() {
        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID, TestConstants.QUANTITY_50))));

        when(productRepository.existsByCode(TestConstants.PRODUCT_CODE)).thenReturn(false);
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.empty());

        try {
            productService.create(requestDTO);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.RAW_MATERIAL_NOT_FOUND + TestConstants.RAW_MATERIAL_ID, ex.getMessage());
        }
    }

    @Test
    void testUpdateProductSuccessfully() {
        Product existing = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED);

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);
        when(productMapper.toResponseDTO(existing))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED));

        ProductResponseDTO response = productService.update(TestConstants.PRODUCT_ID, requestDTO);

        assertNotNull(response);
        assertEquals("Car Updated", response.getName());
    }

    @Test
    void testUpdateProductWithMaterialsSuccessfully() {
        Product existing = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID_TWO, TestConstants.QUANTITY_10))));

        RawMaterial rawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID_TWO, TestConstants.RAW_MATERIAL_NAME, 
                TestConstants.RAW_MATERIAL_CODE, TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(existing));
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID_TWO)).thenReturn(Optional.of(rawMaterial));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(productMapper.toResponseDTO(any(Product.class)))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED));

        ProductResponseDTO response = productService.update(TestConstants.PRODUCT_ID, requestDTO);

        assertNotNull(response);
        assertEquals(1, existing.getMaterials().size());
        ProductMaterial pm = existing.getMaterials().get(0);
        assertEquals(TestConstants.RAW_MATERIAL_ID_TWO, pm.getRawMaterial().getId());
        assertEquals(TestConstants.QUANTITY_10, pm.getQuantityRequired());
    }

    @Test
    void testUpdateProductReplacesExistingMaterialWithoutDuplication() {
        RawMaterial rawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID_TWO, TestConstants.RAW_MATERIAL_NAME, 
                TestConstants.RAW_MATERIAL_CODE, TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        Product existing = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);
        ProductMaterial oldPm = ProductTestFixture.createProductMaterial(1L, existing, rawMaterial, TestConstants.QUANTITY_50);
        existing.getMaterials().add(oldPm);

        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID_TWO, TestConstants.QUANTITY_10))));

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(existing));
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID_TWO)).thenReturn(Optional.of(rawMaterial));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(productMapper.toResponseDTO(any(Product.class)))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, "Car Updated", TestConstants.PRODUCT_PRICE_UPDATED));

        ProductResponseDTO response = productService.update(TestConstants.PRODUCT_ID, requestDTO);

        assertNotNull(response);
        assertEquals(1, existing.getMaterials().size());
        ProductMaterial pm = existing.getMaterials().get(0);
        assertEquals(TestConstants.QUANTITY_10, pm.getQuantityRequired());
        assertNotSame(oldPm, pm);
    }

    @Test
    void testUpdateNonExistentProductThrowsException() {
        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.empty());

        try {
            productService.update(TestConstants.PRODUCT_ID, requestDTO);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.PRODUCT_NOT_FOUND + TestConstants.PRODUCT_ID, ex.getMessage());
        }
    }

    @Test
    void testUpdateProductWithNonExistentMaterialThrowsException() {
        Product existing = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        ProductRequestDTO requestDTO = ProductTestFixture.createProductRequest(
                TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE,
                new ArrayList<>(List.of(ProductTestFixture.createMaterialRequest(
                        TestConstants.RAW_MATERIAL_ID, TestConstants.QUANTITY_50))));

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(existing));
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.empty());

        try {
            productService.update(TestConstants.PRODUCT_ID, requestDTO);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.RAW_MATERIAL_NOT_FOUND + TestConstants.RAW_MATERIAL_ID, ex.getMessage());
        }
    }

    @Test
    void testFindAllProductsSuccessfully() {
        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productRepository.findAllWithMaterials()).thenReturn(new ArrayList<>(List.of(product)));
        when(productMapper.toResponseDTO(product))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                        TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE));

        List<ProductResponseDTO> response = productService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testFindByIdSuccessfully() {
        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE);

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDTO(product))
                .thenReturn(ProductTestFixture.createProductResponse(
                        TestConstants.PRODUCT_ID, TestConstants.PRODUCT_CODE, 
                        TestConstants.PRODUCT_NAME, TestConstants.PRODUCT_PRICE));

        ProductResponseDTO response = productService.findById(TestConstants.PRODUCT_ID);

        assertNotNull(response);
        assertEquals(TestConstants.PRODUCT_CODE, response.getCode());
    }

    @Test
    void testFindNonExistentProductThrowsException() {
        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.empty());

        try {
            productService.findById(TestConstants.PRODUCT_ID);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.PRODUCT_NOT_FOUND + TestConstants.PRODUCT_ID, ex.getMessage());
        }
    }

    @Test
    void testDeleteProductSuccessfully() {
        when(productRepository.existsById(TestConstants.PRODUCT_ID)).thenReturn(true);

        productService.delete(TestConstants.PRODUCT_ID);
    }

    @Test
    void testDeleteNonExistentProductThrowsException() {
        when(productRepository.existsById(TestConstants.PRODUCT_ID)).thenReturn(false);

        try {
            productService.delete(TestConstants.PRODUCT_ID);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.PRODUCT_NOT_FOUND + TestConstants.PRODUCT_ID, ex.getMessage());
        }
    }
}
