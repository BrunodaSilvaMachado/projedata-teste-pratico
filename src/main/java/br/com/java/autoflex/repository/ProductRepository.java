package br.com.java.autoflex.repository;

import br.com.java.autoflex.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.materials pm
        LEFT JOIN FETCH pm.rawMaterial
            """)
    List<Product> findAllWithMaterials();
    
}
