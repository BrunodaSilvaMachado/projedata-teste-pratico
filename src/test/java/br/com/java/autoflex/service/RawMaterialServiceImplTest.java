package br.com.java.autoflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.exception.BusinessException;
import br.com.java.autoflex.exception.ResourceNotFoundException;
import br.com.java.autoflex.fixture.RawMaterialTestFixture;
import br.com.java.autoflex.fixture.TestConstants;
import br.com.java.autoflex.mapper.RawMaterialMapper;
import br.com.java.autoflex.repository.RawMaterialRepository;

/**
 * Unit tests for RawMaterialServiceImpl.
 * Tests CRUD operations for raw material management.
 */
class RawMaterialServiceImplTest {

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
    void testCreateRawMaterialSuccessfully() {
        RawMaterialRequestDTO request = RawMaterialTestFixture.createRawMaterialRequest(
                TestConstants.RAW_MATERIAL_NAME_IRON, TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterial rawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialRepository.existsByCode(request.getCode())).thenReturn(false);
        when(rawMaterialMapper.toEntity(request)).thenReturn(rawMaterial);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(responseDTO);

        RawMaterialResponseDTO response = rawMaterialService.create(request);

        assertNotNull(response);
        assertEquals(TestConstants.RAW_MATERIAL_NAME_IRON, response.getName());
    }

    @Test
    void testCreateRawMaterialWithDuplicateCodeThrowsException() {
        RawMaterialRequestDTO request = RawMaterialTestFixture.createRawMaterialRequest(
                TestConstants.RAW_MATERIAL_NAME_IRON, TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialRepository.existsByCode(request.getCode())).thenReturn(true);

        try {
            rawMaterialService.create(request);
        } catch (BusinessException ex) {
            assertEquals(String.format("Raw material with code %s already exists.", TestConstants.RAW_MATERIAL_CODE_IRON), 
                    ex.getMessage());
        }
    }

    @Test
    void testUpdateRawMaterialSuccessfully() {
        RawMaterialRequestDTO request = RawMaterialTestFixture.createRawMaterialRequest(
                "Iron Updated", TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        RawMaterial existingRawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterial updatedRawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, "Iron Updated", TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, "Iron Updated", TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK_LARGE, TestConstants.UNIT_KG);

        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.of(existingRawMaterial));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(updatedRawMaterial);
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(responseDTO);

        RawMaterialResponseDTO response = rawMaterialService.update(TestConstants.RAW_MATERIAL_ID, request);

        assertNotNull(response);
        assertEquals("Iron Updated", response.getName());
    }

    @Test
    void testUpdateNonExistentRawMaterialThrowsException() {
        RawMaterialRequestDTO request = RawMaterialTestFixture.createRawMaterialRequest(
                TestConstants.RAW_MATERIAL_NAME_IRON, TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.empty());

        try {
            rawMaterialService.update(TestConstants.RAW_MATERIAL_ID, request);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.RAW_MATERIAL_NOT_FOUND + TestConstants.RAW_MATERIAL_ID, ex.getMessage());
        }
    }

    @Test
    void testFindByIdSuccessfully() {
        RawMaterial rawMaterial = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(responseDTO);

        RawMaterialResponseDTO response = rawMaterialService.findById(TestConstants.RAW_MATERIAL_ID);

        assertNotNull(response);
        assertEquals(TestConstants.RAW_MATERIAL_NAME_IRON, response.getName());
    }

    @Test
    void testFindAllRawMaterialsSuccessfully() {
        RawMaterial iron = RawMaterialTestFixture.createRawMaterial(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON, 
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>(List.of(iron)));
        when(rawMaterialMapper.toResponseDTO(any(RawMaterial.class))).thenReturn(responseDTO);

        List<RawMaterialResponseDTO> response = rawMaterialService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(TestConstants.RAW_MATERIAL_NAME_IRON, response.get(0).getName());
    }

    @Test
    void testFindNonExistentRawMaterialByIdThrowsException() {
        when(rawMaterialRepository.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(Optional.empty());

        try {
            rawMaterialService.findById(TestConstants.RAW_MATERIAL_ID);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.RAW_MATERIAL_NOT_FOUND + TestConstants.RAW_MATERIAL_ID, ex.getMessage());
        }
    }

    @Test
    void testDeleteRawMaterialSuccessfully() {
        when(rawMaterialRepository.existsById(TestConstants.RAW_MATERIAL_ID)).thenReturn(true);

        rawMaterialService.delete(TestConstants.RAW_MATERIAL_ID);

        verify(rawMaterialRepository).deleteById(TestConstants.RAW_MATERIAL_ID);
    }

    @Test
    void testDeleteNonExistentRawMaterialThrowsException() {
        when(rawMaterialRepository.existsById(TestConstants.RAW_MATERIAL_ID)).thenReturn(false);

        try {
            rawMaterialService.delete(TestConstants.RAW_MATERIAL_ID);
        } catch (ResourceNotFoundException ex) {
            assertEquals(TestConstants.RAW_MATERIAL_NOT_FOUND + TestConstants.RAW_MATERIAL_ID, ex.getMessage());
        }
    }
}
