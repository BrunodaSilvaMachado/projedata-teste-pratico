package br.com.java.autoflex.service;

import br.com.java.autoflex.dto.RawMaterialResponseDTO;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import java.util.List;

public interface RawMaterialService {
    
    RawMaterialResponseDTO create(RawMaterialRequestDTO request);
    RawMaterialResponseDTO update(Long id, RawMaterialRequestDTO request);
    void delete(Long id);
    RawMaterialResponseDTO findById(Long id);
    List<RawMaterialResponseDTO> findAll();

}
