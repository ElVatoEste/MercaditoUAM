package com.example.mercaditouam.service;

import com.example.mercaditouam.model.Estudiante;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IServiceEstudiante {
    public List<Estudiante> getAll();
    public void crearEstudiante(Estudiante estudiante);
    public void eliminarEstudiante(Integer id);
}
