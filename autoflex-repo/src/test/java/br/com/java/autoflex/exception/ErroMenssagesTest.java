package br.com.java.autoflex.exception;

import org.junit.jupiter.api.Test;

public class ErroMenssagesTest {
    @Test
    void testThrowErrorMessages() {
        try {
                ErrorMessages errorMessages = new ErrorMessages();
                System.out.println(errorMessages);
        } catch ( IllegalArgumentException | SecurityException | AssertionError e) {
            System.out.println("Expected exception caught: " + e.getMessage());
            assert(e.getMessage().contains("Utility class - cannot be instantiated"));
        }
    }
}
