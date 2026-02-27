package br.com.java.autoflex.domain;

import java.math.BigDecimal;

import br.com.java.autoflex.dto.RawMaterialRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "raw_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private BigDecimal stockQuantity;
    @Column(nullable = false)
    private String unit;

    public void updateFromRequest(RawMaterialRequestDTO request) {
        this.name = request.getName();
        this.code = request.getCode();
        this.stockQuantity = request.getStockQuantity();
        this.unit = request.getUnit();
    }
}
