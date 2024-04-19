package com.example.mercaditouam.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Estudiante {
    @Id
    private Integer id;

    private String nombre;

    private String apellido;

    private String cif;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Publicacion> publicacion;

    // Relación uno a muchos con la entidad Compra como comprador
    @OneToMany(mappedBy = "estudianteComprador", cascade = CascadeType.ALL)
    private List<Compra> comprasComoComprador;

    // Relación uno a muchos con la entidad Compra como vendedor
    @OneToMany(mappedBy = "estudianteVendedor", cascade = CascadeType.ALL)
    private List<Compra> comprasComoVendedor;
}
