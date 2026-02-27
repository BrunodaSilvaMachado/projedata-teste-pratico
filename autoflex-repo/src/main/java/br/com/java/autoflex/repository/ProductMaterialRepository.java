package br.com.java.autoflex.repository;

import br.com.java.autoflex.domain.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
    Optional<ProductMaterial> findByProductIdAndRawMaterialId(Long productId, Long rawMaterialId);

    boolean existsByProductIdAndRawMaterialId(Long productId, Long rawMaterialId);
}