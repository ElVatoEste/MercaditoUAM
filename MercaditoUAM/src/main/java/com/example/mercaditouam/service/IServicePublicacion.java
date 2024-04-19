package com.example.mercaditouam.service;

import com.example.mercaditouam.model.Estudiante;
import com.example.mercaditouam.model.Publicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IServicePublicacion {
    public List<Publicacion> getAll();
    public void crearPublicacion(Publicacion publicacion);
    public void eliminarPublicacion(Integer id);
}
