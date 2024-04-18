package com.example.mercaditouam.repository;

import com.example.mercaditouam.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepoEstudiante extends JpaRepository<Estudiante, Integer> {
}
