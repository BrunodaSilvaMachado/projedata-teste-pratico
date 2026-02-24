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
            throw new BusinessException("Product code already exists");
        }
        Product product = Product.builder()
                .code(productRequestDTO.getCode())
                .name(productRequestDTO.getName())
                .price(productRequestDTO.getPrice())
                .materials(new ArrayList<>())
                .build();
        for (ProductMaterialRequestDTO materialDTO: productRequestDTO.getMaterials()) {
            RawMaterial material = rawMaterialRepository.findById(materialDTO.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + materialDTO.getRawMaterialId()));
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
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        existing.setCode(productRequestDTO.getCode());
        existing.setName(productRequestDTO.getName());
        existing.setPrice(productRequestDTO.getPrice());
        existing.setMaterials(new ArrayList<>());

        for (ProductMaterialRequestDTO materialDTO: productRequestDTO.getMaterials()) {
            RawMaterial material = rawMaterialRepository.findById(materialDTO.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + materialDTO.getRawMaterialId()));
            ProductMaterial productMaterial = ProductMaterial.builder()
                    .product(existing)
                    .rawMaterial(material)
                    .quantityRequired(materialDTO.getQuantityRequired())
                    .build();
            existing.getMaterials().add(productMaterial);
        }
        //TODO: Melhorar lógica de update para não apagar e recriar materiais, mas sim atualizar os existentes
        Product savedProduct = productRepository.save(existing);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
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
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
