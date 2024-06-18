package com.araujo.leandro.desafiofinal.clubes.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ClubeDTO implements Serializable {
    private static final long serialVersionUID = 14304343943493430L;
    private Long id;
    private String nome;
    private String estado;
    private Date dataCriacao;
    private boolean ativo;
}
