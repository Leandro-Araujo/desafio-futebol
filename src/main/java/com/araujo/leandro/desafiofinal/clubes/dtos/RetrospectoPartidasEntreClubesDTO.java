package com.araujo.leandro.desafiofinal.clubes.dtos;

import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaResponseDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RetrospectoPartidasEntreClubesDTO {
    private List<PartidaResponseDTO> partidas;
    private String nomeClubeAdversario;
    private RetrospectoClubeDTO clubePesquisado;
}
