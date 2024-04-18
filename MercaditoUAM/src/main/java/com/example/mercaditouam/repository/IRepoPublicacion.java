package com.example.mercaditouam.repository;

import com.example.mercaditouam.model.Estudiante;
import com.example.mercaditouam.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepoPublicacion extends JpaRepository<Publicacion, Integer> {
}
