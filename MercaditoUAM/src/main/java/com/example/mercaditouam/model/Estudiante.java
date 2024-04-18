package com.example.mercaditouam.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
}
