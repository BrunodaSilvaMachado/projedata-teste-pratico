package br.com.java.autoflex.fixture;

import java.math.BigDecimal;

/**
 * Common test constants used across test suites.
 * Centralizes magic values to improve maintainability.
 */
public final class TestConstants {
    private TestConstants() {
        throw new AssertionError("Utility class - cannot be instantiated");
    }

    // Product constants
    public static final Long PRODUCT_ID = 1L;
    public static final Long PRODUCT_ID_TWO = 2L;
    public static final String PRODUCT_CODE = "CAR01";
    public static final String PRODUCT_NAME = "Car";
    public static final BigDecimal PRODUCT_PRICE = new BigDecimal("1000");
    public static final BigDecimal PRODUCT_PRICE_UPDATED = new BigDecimal("1200");
    public static final BigDecimal PRODUCT_PRICE_LOW = new BigDecimal("500");

    // Raw Material constants
    public static final Long RAW_MATERIAL_ID = 1L;
    public static final Long RAW_MATERIAL_ID_TWO = 2L;
    public static final String RAW_MATERIAL_NAME = "Steel";
    public static final String RAW_MATERIAL_CODE = "STL01";
    public static final String RAW_MATERIAL_NAME_IRON = "Iron";
    public static final String RAW_MATERIAL_CODE_IRON = "IRON01";
    public static final BigDecimal RAW_MATERIAL_STOCK = new BigDecimal("100");
    public static final BigDecimal RAW_MATERIAL_STOCK_LARGE = new BigDecimal("250");
    public static final BigDecimal RAW_MATERIAL_STOCK_ZERO = BigDecimal.ZERO;
    public static final String UNIT_KG = "kg";

    // Material quantities
    public static final BigDecimal QUANTITY_50 = new BigDecimal("50");
    public static final BigDecimal QUANTITY_10 = new BigDecimal("10");
    public static final BigDecimal QUANTITY_100 = new BigDecimal("100");
    public static final BigDecimal QUANTITY_ZERO = BigDecimal.ZERO;

    // Messages
    public static final String PRODUCT_CODE_ALREADY_EXISTS = "Product code already exists";
    public static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    public static final String RAW_MATERIAL_NOT_FOUND = "Raw material not found with id: ";
}
