package br.com.java.autoflex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.service.RawMaterialService;

public class RawMaterialControllerTest {
    private RawMaterialService rawMaterialService;
    private RawMaterialController rawMaterialController;

    @BeforeEach
    public void setUp() {
        rawMaterialService = mock(RawMaterialService.class);
        rawMaterialController = new RawMaterialController(rawMaterialService);
    }

    @Test
    public void shouldCreateRawMaterialSuccessfully() {
        RawMaterialResponseDTO responseDTO = new RawMaterialResponseDTO(1L, "Steel","STL01", new BigDecimal("100"), "kg");
        when(rawMaterialService.create(null)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.create(null);
        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
    }

    @Test
    public void shouldFindByIdSuccessfully() {
        RawMaterialResponseDTO responseDTO = new RawMaterialResponseDTO(1L, "Steel","STL01", new BigDecimal("100"), "kg");
        when(rawMaterialService.findById(1L)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.findById(1L);
        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
    }

    @Test
    public void shouldFindAllRawMaterialsSuccessfully() {
        RawMaterialResponseDTO responseDTO = new RawMaterialResponseDTO(1L, "Steel","STL01", new BigDecimal("100"), "kg");
        when(rawMaterialService.findAll()).thenReturn(List.of(responseDTO));

        List<RawMaterialResponseDTO> rawMaterials = rawMaterialController.findAll();
        assertNotNull(rawMaterials);
        assertTrue(rawMaterials.size() > 0);
        assertEquals(1L, rawMaterials.get(0).getId());
    }

    @Test
    public void shouldUpdateRawMaterialSuccessfully() {
        RawMaterialResponseDTO responseDTO = new RawMaterialResponseDTO(1L, "Steel","STL01", new BigDecimal("100"), "kg");
        when(rawMaterialService.update(1L, null)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.update(1L, null);
        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
    }

    @Test
    public void shouldDeleteRawMaterialSuccessfully() {
       rawMaterialController.delete(1L);
       verify(rawMaterialService).delete(1L);
    }
}
