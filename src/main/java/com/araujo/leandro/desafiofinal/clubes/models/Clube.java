package com.araujo.leandro.desafiofinal.clubes.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clube")
public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "data_criacao", nullable = false)
    private Date dataCriacao;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    public Clube() {
    }

    public Clube(String nome, String estado, Date dataCriacao, boolean ativo) {
        this.setNome(nome);
        this.setEstado(estado);
        this.setDataCriacao(dataCriacao);
        this.ativo = ativo;
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
        if(nome.length() < 2)
            throw new IllegalArgumentException("Nome do clube deve ter no mínimo 2 caracteres");
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        List<String> ufBrasil = List.of("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");
        if(!ufBrasil.contains(estado))
            throw new IllegalArgumentException("Estado inválido");
        this.estado = estado;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        if(dataCriacao.after(new Date()))
            throw new IllegalArgumentException("Data de criação não pode ser futura");
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Clube{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", estado='" + estado + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", ativo=" + ativo +
                '}';
    }
}
