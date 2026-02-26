package br.com.java.autoflex.fixture;

import java.math.BigDecimal;

import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;

/**
 * Test fixture for creating RawMaterial-related test objects.
 * Provides factory methods to reduce code duplication.
 */
public class RawMaterialTestFixture {

    public static RawMaterial createRawMaterial(Long id, String name, String code, BigDecimal stockQuantity,
            String unit) {
        return new RawMaterial(id, name, code, stockQuantity, unit);
    }

    public static RawMaterialRequestDTO createRawMaterialRequest(String name, String code, 
            BigDecimal stockQuantity, String unit) {
        RawMaterialRequestDTO request = new RawMaterialRequestDTO();
        request.setName(name);
        request.setCode(code);
        request.setStockQuantity(stockQuantity);
        request.setUnit(unit);
        return request;
    }

    public static RawMaterialResponseDTO createRawMaterialResponse(Long id, String name, String code,
            BigDecimal stockQuantity, String unit) {
        return new RawMaterialResponseDTO(id, name, code, stockQuantity, unit);
    }
}
