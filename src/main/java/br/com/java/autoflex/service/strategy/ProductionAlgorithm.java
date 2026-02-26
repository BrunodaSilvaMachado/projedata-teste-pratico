package br.com.java.autoflex.service.strategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;

/**
 * Strategy interface for production suggestion algorithms.
 * Implementations define various strategies to calculate which products
 * should be produced based on available inventory and business rules.
 */
public interface ProductionAlgorithm {

    /**
     * Generates production suggestions based on available stock.
     *
     * @param products list of products to consider for production
     * @param availableStock map of raw material IDs to their available quantities
     * @return a response DTO containing production suggestions and total value
     */
    ProductionSuggestionResponseDTO generateSuggestions(
            List<Product> products,
            Map<Long, BigDecimal> availableStock
    );
}
