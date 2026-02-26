package br.com.java.autoflex.exception;

public final class ErrorMessages {
    public ErrorMessages() {
        throw new AssertionError("Utility class - cannot be instantiated");
    }

    // Product messages
    public static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    public static final String PRODUCT_CODE_ALREADY_EXISTS = "Product code already exists";

    // Raw Material messages
    public static final String RAW_MATERIAL_NOT_FOUND = "Raw material not found with id: ";
    public static final String RAW_MATERIAL_CODE_ALREADY_EXISTS = "Raw material with code %s already exists.";

    // Production messages
    public static final String INSUFFICIENT_STOCK = "Insufficient stock for material: ";
}
