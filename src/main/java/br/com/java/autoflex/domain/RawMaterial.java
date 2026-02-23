package br.com.java.autoflex.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private Double stockQuantity;
    @Column(nullable = false)
    private String unit;  
}
