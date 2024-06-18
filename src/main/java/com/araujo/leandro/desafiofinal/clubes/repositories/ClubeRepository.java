package com.araujo.leandro.desafiofinal.clubes.repositories;

import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    Optional<Clube> findByNomeAndEstado(String nome, String estado);
    Page<Clube> findByNomeContainingAndEstadoContainingAndAtivo(String nome, String estado, Boolean ativo, Pageable pageable);
    Page<Clube> findByNomeContainingAndEstadoContaining(String nome, String estado, Pageable pageable);
}
