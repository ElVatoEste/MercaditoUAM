package com.example.mercaditouam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_comprador_id")
    private Estudiante estudianteComprador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_vendedor_id")
    private Estudiante estudianteVendedor;

    private double monto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;  // Publicación comprada

    private LocalDateTime fechaCompra;

    private Double precio;  // Precio de la compra
}