package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.fixture.ProductTestFixture;
import br.com.java.autoflex.fixture.RawMaterialTestFixture;
import br.com.java.autoflex.fixture.TestConstants;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;

/**
 * Unit tests for ProductionServiceImpl.
 * Tests production suggestion algorithm with various stock scenarios.
 */
class ProductionServiceImplTest {
    private ProductRepository productRepository;
    private RawMaterialRepository rawMaterialRepository;
    private ProductionServiceImpl productionService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        rawMaterialRepository = mock(RawMaterialRepository.class);
        productionService = new ProductionServiceImpl(productRepository, rawMaterialRepository);
    }

    @Test
    void testProduceOnlyMostExpensiveProductWhenStockIsLimited() {
        RawMaterial iron = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, "Iron", "IRON01", TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        Product expensiveProduct = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "CAR01", "Car", TestConstants.PRODUCT_PRICE);
        ProductMaterial pmExpensive = ProductTestFixture.createProductMaterial(
                1L, expensiveProduct, iron, TestConstants.QUANTITY_50);
        expensiveProduct.getMaterials().add(pmExpensive);

        Product cheapProduct = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID_TWO, "BIKE01", "Bike", TestConstants.PRODUCT_PRICE_LOW);
        ProductMaterial pmCheap = ProductTestFixture.createProductMaterial(
                2L, cheapProduct, iron, TestConstants.QUANTITY_50);
        cheapProduct.getMaterials().add(pmCheap);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(expensiveProduct, cheapProduct)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertEquals(1, response.getItems().size());
        assertEquals("Car", response.getItems().get(0).getProductName());
    }

    @Test
    void testReturnEmptyWhenNoStockAvailable() {
        RawMaterial ironWithoutStock = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, "Iron", "IRON01", TestConstants.RAW_MATERIAL_STOCK_ZERO, TestConstants.UNIT_KG);

        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "CAR01", "Car", TestConstants.PRODUCT_PRICE);
        ProductMaterial pm = ProductTestFixture.createProductMaterial(
                1L, product, ironWithoutStock, TestConstants.QUANTITY_50);
        product.getMaterials().add(pm);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(product)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(ironWithoutStock)));

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testProduceMultipleProductsWhenStockAllows() {
        RawMaterial iron = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, "Iron", "IRON01", TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        Product expensiveProduct = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "CAR01", "Car", TestConstants.PRODUCT_PRICE);
        ProductMaterial pmExpensive = ProductTestFixture.createProductMaterial(
                1L, expensiveProduct, iron, TestConstants.QUANTITY_100);
        expensiveProduct.getMaterials().add(pmExpensive);

        Product cheapProduct = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID_TWO, "BIKE01", "Bike", TestConstants.PRODUCT_PRICE_LOW);
        ProductMaterial pmCheap = ProductTestFixture.createProductMaterial(
                2L, cheapProduct, iron, TestConstants.QUANTITY_50);
        cheapProduct.getMaterials().add(pmCheap);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(expensiveProduct, cheapProduct)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertEquals(2, response.getItems().size());
    }

    @Test
    void testLimitProductionByMostRestrictiveRawMaterial() {
        RawMaterial iron = RawMaterialTestFixture.createRawMaterial(
                1L, "Iron", "IRON01", TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);
        RawMaterial plastic = RawMaterialTestFixture.createRawMaterial(
                2L, "Plastic", "PLASTIC01", TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "CAR01", "Car", TestConstants.PRODUCT_PRICE);
        ProductMaterial ironMaterial = ProductTestFixture.createProductMaterial(
                1L, product, iron, TestConstants.QUANTITY_100);
        ProductMaterial plasticMaterial = ProductTestFixture.createProductMaterial(
                2L, product, plastic, TestConstants.QUANTITY_50);
        product.getMaterials().add(ironMaterial);
        product.getMaterials().add(plasticMaterial);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(product)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron, plastic)));

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertEquals(1, response.getItems().size());
        assertEquals(2, response.getItems().get(0).getQuantityToProduce());
    }

    @Test
    void testIgnoreProductWithoutMaterials() {
        Product productWithoutMaterials = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "SERVICE01", "Service", TestConstants.PRODUCT_PRICE);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(productWithoutMaterials)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>());

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testIgnoreProductWithNullMaterials() {
        Product productWithNullMaterials = new Product(TestConstants.PRODUCT_ID, "SERVICE01", "Service", 
                TestConstants.PRODUCT_PRICE, null);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(productWithNullMaterials)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>());

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void testHandleProductsWithZeroRequiredQuantity() {
        RawMaterial iron = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, "Iron", "IRON01", TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        Product product = ProductTestFixture.createProduct(
                TestConstants.PRODUCT_ID, "CAR01", "Car", TestConstants.PRODUCT_PRICE);
        ProductMaterial pm = ProductTestFixture.createProductMaterial(
                1L, product, iron, TestConstants.QUANTITY_ZERO);
        product.getMaterials().add(pm);

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(product)));
        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));

        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();

        assertEquals(1, response.getItems().size());
    }
}
