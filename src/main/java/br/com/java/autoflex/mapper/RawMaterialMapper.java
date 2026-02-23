package br.com.java.autoflex.mapper;

import org.mapstruct.Mapper;

import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import br.com.java.autoflex.dto.RawMaterialResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RawMaterialMapper {
    RawMaterial toEntity(RawMaterialRequestDTO dto);

    RawMaterialResponseDTO toResponseDTO(RawMaterial entity);
}
