package com.example.mercaditouam.service;

import com.example.mercaditouam.model.Estudiante;
import com.example.mercaditouam.model.Publicacion;
import com.example.mercaditouam.repository.IRepoEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ServiceEstudiante implements IServiceEstudiante{
    @Autowired
    private IRepoEstudiante repo;
    @Override
    public List<Estudiante> getAll() {
        return repo.findAll();
    }

    @Override
    public void crearEstudiante(Estudiante estudiante) {
        List<Publicacion> lista = estudiante.getPublicacion();
        for (Publicacion p : lista) {
            p.setEstudiante(estudiante);
        }
        repo.save(estudiante);
    }

    @Override
    public void eliminarEstudiante(Integer id) {
        repo.deleteById(id);
    }
}
