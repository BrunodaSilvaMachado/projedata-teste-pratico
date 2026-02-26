package br.com.java.autoflex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.ProductMaterialRequestDTO;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;
import br.com.java.autoflex.exception.BusinessException;
import br.com.java.autoflex.exception.ErrorMessages;
import br.com.java.autoflex.exception.ResourceNotFoundException;
import br.com.java.autoflex.mapper.ProductMapper;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService  {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        if (productRepository.existsByCode(productRequestDTO.getCode())){
            throw new BusinessException(ErrorMessages.PRODUCT_CODE_ALREADY_EXISTS);
        }
        Product product = Product.builder()
                .code(productRequestDTO.getCode())
                .name(productRequestDTO.getName())
                .price(productRequestDTO.getPrice())
                .materials(new ArrayList<>())
                .build();
        for (ProductMaterialRequestDTO materialDTO: productRequestDTO.getMaterials()) {
            RawMaterial material = rawMaterialRepository.findById(materialDTO.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RAW_MATERIAL_NOT_FOUND + materialDTO.getRawMaterialId()));
            ProductMaterial productMaterial = ProductMaterial.builder()
                    .product(product)
                    .rawMaterial(material)
                    .quantityRequired(materialDTO.getQuantityRequired())
                    .build();
            product.getMaterials().add(productMaterial);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND + id));
        existing.setCode(productRequestDTO.getCode());
        existing.setName(productRequestDTO.getName());
        existing.setPrice(productRequestDTO.getPrice());
        // clear the existing list so that orphanRemoval can delete rows from the database
        existing.getMaterials().clear();
        // flush to ensure DELETE is executed before INSERT
        productRepository.flush();

        for (ProductMaterialRequestDTO materialDTO: productRequestDTO.getMaterials()) {
            RawMaterial material = rawMaterialRepository.findById(materialDTO.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RAW_MATERIAL_NOT_FOUND + materialDTO.getRawMaterialId()));
            ProductMaterial productMaterial = ProductMaterial.builder()
                    .product(existing)
                    .rawMaterial(material)
                    .quantityRequired(materialDTO.getQuantityRequired())
                    .build();
            existing.getMaterials().add(productMaterial);
        }
        Product savedProduct = productRepository.save(existing);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND + id));
        return productMapper.toResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> findAll() {
        List<Product> products = productRepository.findAllWithMaterials();
        return products.stream().map(productMapper::toResponseDTO).toList();
    }

    @Override
    public void delete(Long id) {
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND + id);
        }
        productRepository.deleteById(id);
    }
}
