package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.exception.BusinessException;
import br.com.java.autoflex.exception.ResourceNotFoundException;
import br.com.java.autoflex.mapper.RawMaterialMapper;
import br.com.java.autoflex.repository.RawMaterialRepository;

public class RawMaterialServiceImplTest {
    
    private RawMaterialRepository rawMaterialRepository;
    private RawMaterialMapper rawMaterialMapper;
    private RawMaterialServiceImpl rawMaterialService;

    @BeforeEach
    void setUp() {
        rawMaterialRepository = mock(RawMaterialRepository.class);
        rawMaterialMapper = mock(RawMaterialMapper.class);
        rawMaterialService = new RawMaterialServiceImpl(rawMaterialRepository, rawMaterialMapper);
    }

    @Test
    void shouldCreateRawMaterialSuccessfully() {
        RawMaterialRequestDTO request = new RawMaterialRequestDTO();
        request.setName("Iron");
        request.setCode("IRON01");
        request.setStockQuantity(new BigDecimal(1000));
        request.setUnit("kg");

        when(rawMaterialRepository.existsByCode(request.getCode())).thenReturn(false);
        when(rawMaterialMapper.toEntity(request)).thenReturn(new RawMaterial(1L,"Iron", "IRON01", new BigDecimal(1000), "kg"));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(new RawMaterial(1L,"Iron", "IRON01", new BigDecimal(1000), "kg"));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(new RawMaterialResponseDTO(1L,"Iron", "IRON01", new BigDecimal(1000), "kg"));

        RawMaterialResponseDTO response = rawMaterialService.create(request);

        assertNotNull(response);
        assertEquals("Iron", response.getName());
    }

    @Test
    void shouldNotCreateRawMaterialWithDuplicateCode() {
        RawMaterialRequestDTO request = new RawMaterialRequestDTO();
        request.setName("Iron");
        request.setCode("IRON01");
        request.setStockQuantity(new BigDecimal(1000));
        request.setUnit("kg");

        when(rawMaterialRepository.existsByCode(request.getCode())).thenReturn(true);

        try {
            rawMaterialService.create(request);
        } catch (BusinessException e) {
            assertEquals("Raw material with code IRON01 already exists.", e.getMessage());
        }
    }

    @Test
    void shouldUpdateRawMaterialSuccessfully() {
        Long id = 1L;
        RawMaterialRequestDTO request = new RawMaterialRequestDTO();
        request.setName("Iron Updated");
        request.setCode("IRON01");
        request.setStockQuantity(new BigDecimal(2000));
        request.setUnit("kg");

        RawMaterial existingRawMaterial = new RawMaterial(id,"Iron", "IRON01", new BigDecimal(1000), "kg");

        when(rawMaterialRepository.findById(id)).thenReturn(java.util.Optional.of(existingRawMaterial));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(new RawMaterial(id,"Iron Updated", "IRON01", new BigDecimal(2000), "kg"));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(new RawMaterialResponseDTO(id,"Iron Updated", "IRON01", new BigDecimal(2000), "kg"));

        RawMaterialResponseDTO response = rawMaterialService.update(id, request);

        assertNotNull(response);
        assertEquals("Iron Updated", response.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingRawMaterial() {
        Long id = 1L;
        RawMaterialRequestDTO request = new RawMaterialRequestDTO();
        request.setName("Iron Updated");
        request.setCode("IRON01");
        request.setStockQuantity(new BigDecimal(2000));
        request.setUnit("kg");

        when(rawMaterialRepository.findById(id)).thenReturn(java.util.Optional.empty());

        try {
            rawMaterialService.update(id, request);
        } catch (ResourceNotFoundException e) {
            assertEquals("Raw material with id 1 not found.", e.getMessage());
        }
    }

    @Test
    void shouldFindByIdSuccessfully() {
        Long id = 1L;
        RawMaterial rawMaterial = new RawMaterial(id,"Iron", "IRON01", new BigDecimal(1000), "kg");

        when(rawMaterialRepository.findById(id)).thenReturn(java.util.Optional.of(rawMaterial));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(new RawMaterialResponseDTO(id,"Iron", "IRON01", new BigDecimal(1000), "kg"));

        RawMaterialResponseDTO response = rawMaterialService.findById(id);

        assertNotNull(response);
        assertEquals("Iron", response.getName());
    }

    @Test
    void shouldFindAllRawMaterials() {
        // Implementar teste para listar todas as matérias-primas
        RawMaterial iron = new RawMaterial(1L, "Iron","IRON01", new BigDecimal(1000), "kg");

        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>(List.of(iron)));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(new RawMaterialResponseDTO(1L,"Iron", "IRON01", new BigDecimal(1000), "kg"));

        List<RawMaterialResponseDTO> response = rawMaterialService.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Iron", response.get(0).getName());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingRawMaterialById() {
        Long id = 1L;
        when(rawMaterialRepository.findById(id)).thenReturn(java.util.Optional.empty());

        try {
            rawMaterialService.findById(id);
        } catch (ResourceNotFoundException e) {
            assertEquals("Raw material with id 1 not found.", e.getMessage());
        }
    }

    @Test
    void shouldDeleteRawMaterialSuccessfully() {
        // Implementar teste para deletar uma matéria-prima
        Long id = 1L;
        when(rawMaterialRepository.existsById(id)).thenReturn(true);

        rawMaterialService.delete(id);

        verify(rawMaterialRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingRawMaterial() {
        Long id = 1L;
        when(rawMaterialRepository.existsById(id)).thenReturn(false);

        try {
            rawMaterialService.delete(id);
        } catch (ResourceNotFoundException e) {
            assertEquals("Raw material with id 1 not found.", e.getMessage());
        }
    }
}
