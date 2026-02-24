package br.com.java.autoflex.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.production.ProductionItemSuggestionDTO;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public ProductionSuggestionResponseDTO generateProductionSuggestionResponseDTO() {
        // 1. Fetch products with materials (avoid N+1 problem)
        List<Product> products = productRepository.findAllWithMaterials();

        // 2. Create virtal stock map for raw materials
        Map<Long, BigDecimal> avaliableStock = new HashMap<>();
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        for (RawMaterial rm : rawMaterials) {
            avaliableStock.put(rm.getId(), rm.getStockQuantity());
        }

        // 3. Sort products by biggest price first
        products.sort(Comparator.comparing(Product::getPrice).reversed());

        List<ProductionItemSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        // 4. browse products
        // TODO: Refatorar os itens 4, 5, 6 em métodos separados para melhorar a legibilidade e manutenção do código.
        for (Product product : products) {

            if (product.getMaterials() == null || product.getMaterials().isEmpty()) {
                continue;
            }

            int maxUnits = Integer.MAX_VALUE;

            // 5. Calculate maximum possible quantity
            for (ProductMaterial pm : product.getMaterials()) {

                Long materialId = pm.getRawMaterial().getId();
                BigDecimal requiredQuantity = pm.getQuantityRequired();

                BigDecimal stock = avaliableStock.getOrDefault(materialId, BigDecimal.ZERO);

                if (requiredQuantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                int possibleUnits = stock.divide(requiredQuantity, 0, RoundingMode.DOWN).intValue();
                maxUnits = Math.min(maxUnits, possibleUnits);
            }
            if(maxUnits <= 0){
                continue;
            }

            // 6. Update virtual stock
            for (ProductMaterial pm : product.getMaterials()) {
                Long materialId = pm.getRawMaterial().getId();
                BigDecimal requiredQuantity = pm.getQuantityRequired();
                BigDecimal totalRequired = requiredQuantity.multiply(BigDecimal.valueOf(maxUnits));
                avaliableStock.put(materialId, avaliableStock.get(materialId).subtract(totalRequired));
            }

            BigDecimal productTotal = product.getPrice().multiply(BigDecimal.valueOf(maxUnits));
            totalValue = totalValue.add(productTotal);

            suggestions.add(new ProductionItemSuggestionDTO(
                    product.getId(),
                    product.getName(),
                    maxUnits,
                    product.getPrice(),
                    productTotal
            ));
        }
        
        return new ProductionSuggestionResponseDTO(suggestions, totalValue);

    }
}
