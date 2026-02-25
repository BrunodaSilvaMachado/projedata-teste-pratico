package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;

public class ProductionServiceImplTest {
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
    void shouldProduceOnlyMostExpensiveProductWhenStockIsLimited(){
        // Matéria-prima
        RawMaterial iron = new RawMaterial(1L, "Iron","IRON01", new BigDecimal("100"), "kg");

        // Produto A (mais caro)
        Product productA = new Product(1L,"CAR01", "Car", new BigDecimal("1000"),new ArrayList<ProductMaterial>());
        ProductMaterial pmA = new ProductMaterial(1L, productA, iron, new BigDecimal("50"));
        productA.setMaterials(new ArrayList<>(List.of(pmA)));

        // Produto B (mais barato)
        Product productB = new Product(2L,"BIKE01", "Bike", new BigDecimal("500"), new ArrayList<ProductMaterial>());
        ProductMaterial pmB = new ProductMaterial(2L, productB, iron, new BigDecimal("50"));
        productB.setMaterials(new ArrayList<>(List.of(pmB)));

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(productA, productB)));

        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));

        ProductionSuggestionResponseDTO response =
                productionService.generateProductionSuggestionResponseDTO();

        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("2000"), response.getTotalValue());

    }

    @Test
    void shouldReturnEmptyWhenNoStockAvailable() {

        RawMaterial iron = new RawMaterial(1L, "Iron","IRON01" ,BigDecimal.ZERO, "kg");

        Product product = new Product(1L,"CAR01", "Car", new BigDecimal("1000"), new ArrayList<ProductMaterial>());
        ProductMaterial pm = new ProductMaterial(1L, product, iron, new BigDecimal("50"));
        product.setMaterials(new ArrayList<>(List.of(pm)));

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(product)));

        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));

        ProductionSuggestionResponseDTO response =
                productionService.generateProductionSuggestionResponseDTO();

        assertTrue(response.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getTotalValue());
    }

    @Test
    void shouldProduceMultipleProductsWhenStockAllows() {

        RawMaterial iron = new RawMaterial(1L, "Iron","IRON01", new BigDecimal("250"), "kg");

        Product productA = new Product(1L,"CAR01", "Car", new BigDecimal("1000"),new ArrayList<ProductMaterial>());
        ProductMaterial pmA = new ProductMaterial(1L, productA, iron, new BigDecimal("100"));
        productA.setMaterials(new ArrayList<>(List.of(pmA)));

        Product productB = new Product(2L,"BIKE01", "Bike", new BigDecimal("500"),new ArrayList<ProductMaterial>());
        ProductMaterial pmB = new ProductMaterial(2L, productB, iron, new BigDecimal("50"));
        productB.setMaterials(new ArrayList<>(List.of(pmB)));

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(productA, productB)));

        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron)));
        ProductionSuggestionResponseDTO response =
                productionService.generateProductionSuggestionResponseDTO();

        assertEquals(2, response.getItems().size());
        assertEquals(new BigDecimal("2500"), response.getTotalValue());
    }

    @Test
    void shouldLimitProductionByLowestAvailableRawMaterial() {

        // Matérias-primas
        RawMaterial iron = new RawMaterial(1L, "Iron","IRON01", new BigDecimal("300"), "kg");
        RawMaterial plastic = new RawMaterial(2L, "Plastic", "PLASTIC01", new BigDecimal("100"), "kg");

        // Produto precisa de:
        // 100 ferro
        // 50 plástico
        Product product = new Product(1L,"CAR01", "Car", new BigDecimal("1000"), new ArrayList<ProductMaterial>());

        ProductMaterial ironMaterial =
                new ProductMaterial(1L, product, iron, new BigDecimal("100"));

        ProductMaterial plasticMaterial =
                new ProductMaterial(2L, product, plastic, new BigDecimal("50"));

        product.setMaterials(new ArrayList<>(List.of(ironMaterial, plasticMaterial)));

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(product)));

        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(iron, plastic)));

        ProductionSuggestionResponseDTO response =
                productionService.generateProductionSuggestionResponseDTO();

        /*
                Ferro permite: 300 / 100 = 3
                Plástico permite: 100 / 50 = 2

                Logo, máximo = 2
        */

        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("2000"), response.getTotalValue());
        assertEquals(2, response.getItems().get(0).getQuantityToProduce());
    }

    @Test
    void shouldIgnoreProductWithoutMaterials() {

        Product productWithoutMaterials =
                new Product(1L, "Service","SERVICE01", new BigDecimal("1000"),new ArrayList<ProductMaterial>());

        productWithoutMaterials.setMaterials(new ArrayList<>()); // vazio

        when(productRepository.findAllWithMaterials())
                .thenReturn(new ArrayList<>(List.of(productWithoutMaterials)));

        when(rawMaterialRepository.findAll())
                .thenReturn(new ArrayList<>());

        ProductionSuggestionResponseDTO response =
                productionService.generateProductionSuggestionResponseDTO();

        assertTrue(response.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getTotalValue());
  }
}
