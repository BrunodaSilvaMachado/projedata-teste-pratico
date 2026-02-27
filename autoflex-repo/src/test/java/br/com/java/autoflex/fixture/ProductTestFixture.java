package br.com.java.autoflex.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.ProductMaterialRequestDTO;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;

/**
 * Test fixture for creating Product-related test objects.
 * Reduces code duplication in tests and provides fluent builders.
 */
public class ProductTestFixture {

    public static ProductRequestDTO createProductRequest(String code, String name, BigDecimal price,
            List<ProductMaterialRequestDTO> materials) {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode(code);
        requestDTO.setName(name);
        requestDTO.setPrice(price);
        requestDTO.setMaterials(materials);
        return requestDTO;
    }

    public static ProductRequestDTO createProductRequest(String code, String name, BigDecimal price) {
        return createProductRequest(code, name, price, new ArrayList<>());
    }

    public static ProductResponseDTO createProductResponse(Long id, String code, String name, BigDecimal price) {
        return new ProductResponseDTO(id, code, name, price, new ArrayList<>());
    }

    public static ProductMaterialRequestDTO createMaterialRequest(Long rawMaterialId, BigDecimal quantity) {
        ProductMaterialRequestDTO materialDTO = new ProductMaterialRequestDTO();
        materialDTO.setRawMaterialId(rawMaterialId);
        materialDTO.setQuantityRequired(quantity);
        return materialDTO;
    }

    public static Product createProduct(Long id, String code, String name, BigDecimal price) {
        return new Product(id, code, name, price, new ArrayList<>());
    }

    public static ProductMaterial createProductMaterial(Long id, Product product, RawMaterial material,
            BigDecimal quantity) {
        return new ProductMaterial(id, product, material, quantity);
    }
}
