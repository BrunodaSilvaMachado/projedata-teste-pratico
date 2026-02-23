package br.com.java.autoflex.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.mapper.RawMaterialMapper;
import br.com.java.autoflex.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;

    @Override
    public RawMaterialResponseDTO create(RawMaterialRequestDTO request) {
        if (rawMaterialRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Raw material with code " + request.getCode() + " already exists.");   
        }

        RawMaterial rawMaterial = rawMaterialMapper.toEntity(request);
        RawMaterial savedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toResponseDTO(savedRawMaterial);
    }

    @Override
    public RawMaterialResponseDTO update(Long id, RawMaterialRequestDTO request) {
        RawMaterial existingRawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raw material with id " + id + " not found."));

        existingRawMaterial.setName(request.getName());
        existingRawMaterial.setCode(request.getCode());
        existingRawMaterial.setStockQuantity(request.getStockQuantity());
        existingRawMaterial.setUnit(request.getUnit());

        RawMaterial updatedRawMaterial = rawMaterialRepository.save(existingRawMaterial);
        return rawMaterialMapper.toResponseDTO(updatedRawMaterial);
    }

    @Override
    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new IllegalArgumentException("Raw material with id " + id + " not found.");
        }
        rawMaterialRepository.deleteById(id);
    }

    @Override
    public RawMaterialResponseDTO findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raw material with id " + id + " not found."));
        return rawMaterialMapper.toResponseDTO(rawMaterial);
    }

    @Override
    public List<RawMaterialResponseDTO> findAll() {
        return rawMaterialRepository.findAll().stream()
                .map(rawMaterialMapper::toResponseDTO)
                .toList();
    }
}
