package br.com.java.autoflex.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFoundExceptionShouldReturnNotFoundAndBody() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product not found with id: 1");
        ResponseEntity<?> response = handler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Product not found with id: 1", body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void handleBusinessExceptionShouldReturnBadRequestAndBody() {
        BusinessException ex = new BusinessException("Product code already exists");
        ResponseEntity<?> response = handler.handleBusinessException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Product code already exists", body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }
}
