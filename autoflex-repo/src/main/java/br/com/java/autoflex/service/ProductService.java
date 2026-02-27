package br.com.java.autoflex.service;

import java.util.List;

import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO productRequestDTO);

    ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO);
    
    ProductResponseDTO findById(Long id);
    
    List<ProductResponseDTO> findAll();

    void delete(Long id);
}
