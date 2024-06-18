package com.araujo.leandro.desafiofinal.estadios.repositories;

import com.araujo.leandro.desafiofinal.estadios.models.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Estadio findByNome(String nome);
}
