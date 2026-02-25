package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import br.com.java.autoflex.dto.ProductMaterialRequestDTO;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;
import br.com.java.autoflex.exception.BusinessException;
import br.com.java.autoflex.exception.ResourceNotFoundException;
import br.com.java.autoflex.mapper.ProductMapper;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;

public class ProductServiceImplTest {
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
    void shouldCreateProductSuccessfully() {
        // Matéria-prima
        RawMaterial iron = new RawMaterial(1L, "Iron", "IRON01", new BigDecimal("100"), "kg");

        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car");
        requestDTO.setPrice(new BigDecimal("1000"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        // Produto esperado
        Product product = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<ProductMaterial>());
        ProductMaterial pm = new ProductMaterial(1L, product, iron, new BigDecimal("50"));
        product.setMaterials(new ArrayList<>(List.of(pm)));

        when(productRepository.existsByCode("CAR01")).thenReturn(false);
        when(rawMaterialRepository.findById(1L)).thenReturn(java.util.Optional.of(iron));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDTO(product))
                .thenReturn(new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>()));

        ProductResponseDTO response = productService.create(requestDTO);

        assertNotNull(response);
    }

    @Test
    void shouldNotCreateProductWithDuplicateCode() {
        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car");
        requestDTO.setPrice(new BigDecimal("1000"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        when(productRepository.existsByCode("CAR01")).thenReturn(true);

        try {
            productService.create(requestDTO);
        } catch (BusinessException e) {
            assertEquals("Product code already exists", e.getMessage());
        }
    }

    @Test
    void shouldNotCreateProductWithNonExistingRawMaterial() {
        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car");
        requestDTO.setPrice(new BigDecimal("1000"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        when(productRepository.existsByCode("CAR01")).thenReturn(false);
        when(rawMaterialRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            productService.create(requestDTO);
        } catch (ResourceNotFoundException e) {
            assertEquals("Raw material not found with id: 1", e.getMessage());
        }
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        // Produto existente
        Product existing = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());

        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car Updated");
        requestDTO.setPrice(new BigDecimal("1200"));
        requestDTO.setMaterials(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);
        when(productMapper.toResponseDTO(existing)).thenReturn(
                new ProductResponseDTO(1L, "CAR01", "Car Updated", new BigDecimal("1200"), new ArrayList<>()));

        ProductResponseDTO response = productService.update(1L, requestDTO);

        assertTrue(response != null);
        assertEquals("Car Updated", response.getName());
    }

    @Test
    void shouldUpdateProductWithMaterials() {
        // Produto existente
        Product existing = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());

        // DTO de requisição com materiais
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car Updated");
        requestDTO.setPrice(new BigDecimal("1200"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(2L);
        materialDTO.setQuantityRequired(new BigDecimal("10"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        // Matéria-prima existente
        RawMaterial raw = new RawMaterial(2L, "Steel", "STL01", new BigDecimal("1000"), "kg");

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(rawMaterialRepository.findById(2L)).thenReturn(java.util.Optional.of(raw));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(productMapper.toResponseDTO(any(Product.class)))
                .thenReturn(new ProductResponseDTO(1L, "CAR01", "Car Updated", new BigDecimal("1200"), new ArrayList<>()));

        ProductResponseDTO response = productService.update(1L, requestDTO);

        assertNotNull(response);
        // o objeto 'existing' deve ter recebido 1 ProductMaterial com os valores corretos
        assertEquals(1, existing.getMaterials().size());
        ProductMaterial pm = existing.getMaterials().get(0);
        assertEquals(2L, pm.getRawMaterial().getId());
        assertEquals(new BigDecimal("10"), pm.getQuantityRequired());
    }

    @Test
    void shouldNotUpdateNonExistingProduct() {
        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car Updated");
        requestDTO.setPrice(new BigDecimal("1200"));
        requestDTO.setMaterials(new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            productService.update(1L, requestDTO);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product not found with id: 1", e.getMessage());
        }
    }

    @Test
    void shouldNotUpdateNonExistingRawMaterial() {
        // DTO de requisição
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode("CAR01");
        requestDTO.setName("Car Updated");
        requestDTO.setPrice(new BigDecimal("1200"));
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(1L);
        materialDTO.setQuantityRequired(new BigDecimal("50"));
        requestDTO.setMaterials(new ArrayList<>(List.of(materialDTO)));

        // Produto existente
        Product existing = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(rawMaterialRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            productService.update(1L, requestDTO);
        } catch (ResourceNotFoundException e) {
            assertEquals("Raw material not found with id: 1", e.getMessage());
        }
    }

    @Test
    void shouldFindAllProductsSuccessfully() {
        // Produto existente
        Product product = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());

        when(productRepository.findAllWithMaterials()).thenReturn(new ArrayList<>(List.of(product)));
        when(productMapper.toResponseDTO(product))
                .thenReturn(new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>()));

        List<ProductResponseDTO> response = productService.findAll();

        assertTrue(response != null);
        assertEquals(1, response.size());
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Produto existente
        Product product = new Product(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>());

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(productMapper.toResponseDTO(product))
                .thenReturn(new ProductResponseDTO(1L, "CAR01", "Car", new BigDecimal("1000"), new ArrayList<>()));

        ProductResponseDTO response = productService.findById(1L);

        assertTrue(response != null);
        assertEquals("CAR01", response.getCode());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingProduct() {
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            productService.findById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product not found with id: 1", e.getMessage());
        }
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingRawMaterial() {
        Long id = 1L;
        when(rawMaterialRepository.existsById(id)).thenReturn(false);

        try {
            productService.delete(id);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product not found with id: 1", e.getMessage());
        }
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.delete(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(false);

        try {
            productService.delete(id);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product not found with id: 1", e.getMessage());
        }
    }

}