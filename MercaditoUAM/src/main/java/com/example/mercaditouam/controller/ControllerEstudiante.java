package com.example.mercaditouam.controller;


import com.example.mercaditouam.model.Estudiante;
import com.example.mercaditouam.service.IServiceEstudiante;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiante")
public class ControllerEstudiante {
    @Autowired
    private IServiceEstudiante serviceEstudiante;

    @GetMapping("/all")
    public List<Estudiante> getAll() {
        return serviceEstudiante.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<String> crearEstudiante(@RequestBody Estudiante estudiante) {
        serviceEstudiante.crearEstudiante(estudiante);
        return ResponseEntity.ok("Usuario creado");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Estudiante estudiante) {
        if(estudiante.getId() == null) {
            return ResponseEntity.badRequest().body("El id no existe");
        }
        serviceEstudiante.crearEstudiante(estudiante);
        return ResponseEntity.ok("Usuario actualizado");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        if(id == null) {
            return  ResponseEntity.badRequest().body("El id no existe");
        }
        serviceEstudiante.eliminarEstudiante(id);
        return ResponseEntity.ok("Usuario eliminado");
    }

}
