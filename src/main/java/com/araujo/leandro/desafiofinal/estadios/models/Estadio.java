package com.araujo.leandro.desafiofinal.estadios.models;

import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import jakarta.persistence.*;

@Entity
@Table(name = "estadio")
public class Estadio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    public Estadio() {
    }

    public Estadio(String nome) {
        this.setNome(nome);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome.length() < 3) throw new IllegalArgumentException("Nome do estadio deve ter no mínimo 3 caracteres");
        this.nome = nome;
    }
}
