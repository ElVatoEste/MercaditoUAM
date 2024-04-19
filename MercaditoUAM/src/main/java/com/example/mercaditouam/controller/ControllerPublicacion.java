package com.example.mercaditouam.controller;


import com.example.mercaditouam.model.Publicacion;
import com.example.mercaditouam.service.IServiceEstudiante;
import com.example.mercaditouam.service.IServicePublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicacion")
public class ControllerPublicacion {
    @Autowired
    private IServicePublicacion servicePublicacion;

    @GetMapping("/all")
    public List<Publicacion> getAll() {
        return servicePublicacion.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<String> crearPublicacion(@RequestBody Publicacion publicacion) {
        servicePublicacion.crearPublicacion(publicacion);
        return ResponseEntity.ok("Publicacion creada");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Publicacion publicacion) {
        if (publicacion.getId() == null) {
            return ResponseEntity.badRequest().body("El id de la publicacion no existe");
        }
        servicePublicacion.crearPublicacion(publicacion);
        return ResponseEntity.ok("Publicacion actualizada");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return  ResponseEntity.badRequest().body("El id de la publicacion no existe");
        }
        servicePublicacion.eliminarPublicacion(id);
        return ResponseEntity.ok("Publicacion eliminada");
    }

}
