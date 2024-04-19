package com.example.mercaditouam.model;

import jakarta.persistence.*;
        import lombok.Data;

@Entity
@Data
public class Publicacion {
    @Id
    private Integer id;

    private String nombre;

    private Integer precio;

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;
}
