package com.example.mercaditouam.service;

import com.example.mercaditouam.model.Publicacion;
import com.example.mercaditouam.repository.IRepoPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePublicacion implements IServicePublicacion{

    @Autowired
    private IRepoPublicacion repoPublicacion;

    @Override
    public List<Publicacion> getAll() {
        return repoPublicacion.findAll();
    }

    @Override
    public void crearPublicacion(Publicacion publicacion) {
        repoPublicacion.save(publicacion);
    }

    @Override
    public void eliminarPublicacion(Integer id) {
        repoPublicacion.deleteById(id);
    }
}
