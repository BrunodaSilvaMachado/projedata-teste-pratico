package br.com.java.autoflex.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;

import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for raw material management.
 * Handles CRUD operations for raw materials.
 */
@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;

    /**
     * Creates a new raw material.
     *
     * @param request Raw material data
     * @return Created raw material
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterialResponseDTO create(@Valid @RequestBody RawMaterialRequestDTO request) {
        return rawMaterialService.create(request);
    }

    /**
     * Retrieves a raw material by ID.
     *
     * @param id Raw material ID
     * @return Raw material details
     */
    @GetMapping("/{id}")
    public RawMaterialResponseDTO findById(@PathVariable Long id) {
        return rawMaterialService.findById(id);
    }

    /**
     * Retrieves all raw materials.
     *
     * @return List of all raw materials
     */
    @GetMapping
    public List<RawMaterialResponseDTO> findAll() {
        return rawMaterialService.findAll();
    }

    /**
     * Updates an existing raw material.
     *
     * @param id Raw material ID
     * @param request Updated raw material data
     * @return Updated raw material
     */
    @PutMapping("/{id}")
    public RawMaterialResponseDTO update(@PathVariable Long id, @Valid @RequestBody RawMaterialRequestDTO request) {
        return rawMaterialService.update(id, request);
    }

    /**
     * Deletes a raw material.
     *
     * @param id Raw material ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
    }
}
