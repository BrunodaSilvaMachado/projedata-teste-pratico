package br.com.java.autoflex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.fixture.RawMaterialTestFixture;
import br.com.java.autoflex.fixture.TestConstants;
import br.com.java.autoflex.service.RawMaterialService;

/**
 * Unit tests for RawMaterialController.
 * Tests REST endpoints for raw material management.
 */
class RawMaterialControllerTest {
    private RawMaterialService rawMaterialService;
    private RawMaterialController rawMaterialController;

    @BeforeEach
    void setUp() {
        rawMaterialService = mock(RawMaterialService.class);
        rawMaterialController = new RawMaterialController(rawMaterialService);
    }

    @Test
    void testCreateRawMaterialSuccessfully() {
        RawMaterialRequestDTO requestDTO = RawMaterialTestFixture.createRawMaterialRequest(
                TestConstants.RAW_MATERIAL_NAME_IRON, TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON,  
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialService.create(requestDTO)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.create(requestDTO);

        assertNotNull(result);
        assertEquals(TestConstants.RAW_MATERIAL_ID, result.getId());
    }

    @Test
    void testFindByIdSuccessfully() {
        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON,  
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialService.findById(TestConstants.RAW_MATERIAL_ID)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.findById(TestConstants.RAW_MATERIAL_ID);

        assertNotNull(result);
        assertEquals(TestConstants.RAW_MATERIAL_ID, result.getId());
    }

    @Test
    void testFindAllRawMaterialsSuccessfully() {
        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON,  
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialService.findAll()).thenReturn(List.of(responseDTO));

        List<RawMaterialResponseDTO> rawMaterials = rawMaterialController.findAll();

        assertNotNull(rawMaterials);
        assertEquals(1, rawMaterials.size());
        assertEquals(TestConstants.RAW_MATERIAL_ID, rawMaterials.get(0).getId());
    }

    @Test
    void testUpdateRawMaterialSuccessfully() {
        RawMaterialRequestDTO requestDTO = RawMaterialTestFixture.createRawMaterialRequest(
                TestConstants.RAW_MATERIAL_NAME_IRON, TestConstants.RAW_MATERIAL_CODE_IRON, 
                TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        RawMaterialResponseDTO responseDTO = RawMaterialTestFixture.createRawMaterialResponse(
                TestConstants.RAW_MATERIAL_ID, TestConstants.RAW_MATERIAL_NAME_IRON,  
                TestConstants.RAW_MATERIAL_CODE_IRON, TestConstants.RAW_MATERIAL_STOCK, TestConstants.UNIT_KG);

        when(rawMaterialService.update(TestConstants.RAW_MATERIAL_ID, requestDTO)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialController.update(TestConstants.RAW_MATERIAL_ID, requestDTO);

        assertNotNull(result);
        assertEquals(TestConstants.RAW_MATERIAL_ID, result.getId());
    }

    @Test
    void testDeleteRawMaterialSuccessfully() {
        rawMaterialController.delete(TestConstants.RAW_MATERIAL_ID);
        verify(rawMaterialService).delete(TestConstants.RAW_MATERIAL_ID);
    }
}
