package br.com.java.autoflex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.dto.ProductMaterialResponseDTO;
import br.com.java.autoflex.dto.ProductRequestDTO;
import br.com.java.autoflex.dto.ProductResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponseDTO toResponseDTO(Product entity);

    @Mapping(target = "rawMaterial", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductMaterial toProductMaterial(ProductRequestDTO dto);

    @Mapping(target = "rawMaterialId", source = "rawMaterial.id")
    @Mapping(target = "rawMaterialName", source = "rawMaterial.name")
    ProductMaterialResponseDTO toProductMaterialResponseDTO(ProductMaterial entity);
}
