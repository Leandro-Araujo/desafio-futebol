package com.araujo.leandro.desafiofinal.clubes.services;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoPartidasClubesDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoPartidasEntreClubesDTO;
import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.clubes.repositories.ClubeRepository;
import com.araujo.leandro.desafiofinal.estadios.dtos.EstadioDTO;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaResponseDTO;
import com.araujo.leandro.desafiofinal.partidas.models.Partida;
import com.araujo.leandro.desafiofinal.partidas.repositories.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClubeRetrospectoService {
    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    public ClubeRetrospectoService(ClubeRepository clubeRepository,
                                   PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    public RetrospectoClubeDTO buscarRetrospectoClube(Long id){
        Clube clube = clubeRepository.findById(id).orElseThrow( ()-> new DadoNaoExiste("Dado nao existe")  );

        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id, id);

        return calcularRetrospectoClube(id, clube, partidas);
    }

    public List<RetrospectoPartidasClubesDTO> buscarRetrospectoAdversarios(Long id){
        Clube clube = clubeRepository.findById(id).orElseThrow( ()-> new DadoNaoExiste("Dado nao existe")  );
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id, id);

        Map<Long, List<Partida>> partidasPorAdversario = partidas.stream().collect(Collectors.groupingBy(partida -> {
            if (partida.getClubeMandante().getId().equals(id)) {
                return partida.getClubeVisitante().getId();
            }
            return partida.getClubeMandante().getId();
        }));

        Map<ClubeDTO, RetrospectoClubeDTO> retro = partidasPorAdversario.entrySet().stream().collect(Collectors.toMap(entry -> {
            Clube adversario = clubeRepository.findById(entry.getKey()).orElseThrow(() -> new DadoNaoExiste("Clube não encontrado"));
            return new ClubeDTO(adversario.getId(), adversario.getNome(), adversario.getEstado(), adversario.getDataCriacao(), adversario.isAtivo());
        }, entry -> {
            return calcularRetrospectoClube(id, clube, entry.getValue());
        }));

        List<RetrospectoPartidasClubesDTO> retrospectoPartidasClubesDTO = retro.entrySet().stream().map(entry -> {
            return RetrospectoPartidasClubesDTO.builder()
                    .idAdversario(entry.getKey().getId())
                    .nomeAdversario(entry.getKey().getNome())
                    .retrospectoClubeDTO(entry.getValue())
                    .build();
        }).collect(Collectors.toList());

        return retrospectoPartidasClubesDTO;
    }

    private RetrospectoClubeDTO calcularRetrospectoClube(Long id, Clube clube, List<Partida> partidas){
        long vitorias = 0;
        long empates = 0;
        long derrotas = 0;
        long golsFeitos = 0;
        long golsSofridos = 0;

        vitorias = partidas.stream().filter(partida -> {
            if (partida.getClubeMandante().getId().equals(id)
                    && partida.getGolsClubeMandante() > partida.getGolsClubeVisitante()) {
                return true;
            }
            if (partida.getClubeVisitante().getId().equals(id)
                    && partida.getGolsClubeVisitante() > partida.getGolsClubeMandante()) {
                return true;
            }
            return false;
        }).count();

        empates = partidas.stream().filter(partida -> {
            if (partida.getGolsClubeMandante() == partida.getGolsClubeVisitante()) {
                return true;
            }
            return false;
        }).count();

        derrotas = partidas.size() - vitorias - empates;

        golsFeitos = partidas.stream().mapToLong(partida -> {
            if (partida.getClubeMandante().getId().equals(id)) {
                return partida.getGolsClubeMandante();
            }
            return partida.getGolsClubeVisitante();
        }).sum();

        golsSofridos = partidas.stream().mapToLong(partida -> {
            if (partida.getClubeMandante().getId().equals(id)) {
                return partida.getGolsClubeVisitante();
            }
            return partida.getGolsClubeMandante();
        }).sum();

        return new RetrospectoClubeDTO(clube.getNome(), vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }

    public RetrospectoPartidasEntreClubesDTO buscarRetrospectoEntreClubes(Long idTimePesquisado, Long idTimeAdversario) {
        Clube clube1 = clubeRepository.findById(idTimePesquisado).orElseThrow(() -> new DadoNaoExiste("Clube não encontrado"));
        Clube clube2 = clubeRepository.findById(idTimeAdversario).orElseThrow(() -> new DadoNaoExiste("Clube não encontrado"));

        List<Partida> partidas = partidaRepository.findByClubeMandanteIdAndClubeVisitanteIdOrClubeMandanteIdAndClubeVisitanteId(
                idTimePesquisado,
                idTimeAdversario,
                idTimePesquisado,
                idTimeAdversario);

        long vitorias = 0;
        long empates = 0;
        long derrotas = 0;
        long golsFeitos = 0;
        long golsSofridos = 0;

        vitorias = partidas.stream().filter(partida -> {
            if (partida.getClubeMandante().getId().equals(idTimePesquisado) && partida.getGolsClubeMandante() > partida.getGolsClubeVisitante()) {
                return true;
            }
            if (partida.getClubeVisitante().getId().equals(idTimePesquisado) && partida.getGolsClubeVisitante() > partida.getGolsClubeMandante()) {
                return true;
            }
            return false;
        }).count();

        empates = partidas.stream().filter(partida -> {
            if (partida.getGolsClubeMandante() == partida.getGolsClubeVisitante()) {
                return true;
            }
            return false;
        }).count();

        derrotas = partidas.size() - vitorias - empates;

        golsFeitos = partidas.stream().mapToLong(partida -> {
            if (partida.getClubeMandante().getId().equals(idTimePesquisado)) {
                return partida.getGolsClubeMandante();
            }
            return partida.getGolsClubeVisitante();
        }).sum();

        golsSofridos = partidas.stream().mapToLong(partida -> {
            if (partida.getClubeMandante().getId().equals(idTimePesquisado)) {
                return partida.getGolsClubeVisitante();
            }
            return partida.getGolsClubeMandante();
        }).sum();

        List<PartidaResponseDTO> partidasDTO = partidas.stream().map(partida -> {
            return PartidaResponseDTO.builder()
                    .id(partida.getId())
                    .dataHora(partida.getDataHora())
                    .clubeMandante(
                            ClubeDTO.builder()
                                    .id(partida.getClubeMandante().getId())
                                    .nome(partida.getClubeMandante().getNome())
                                    .estado(partida.getClubeMandante().getEstado())
                                    .dataCriacao(partida.getClubeMandante().getDataCriacao())
                                    .ativo(partida.getClubeMandante().isAtivo())
                                    .build())
                    .clubeVisitante(ClubeDTO.builder()
                            .id(partida.getClubeVisitante().getId())
                            .nome(partida.getClubeVisitante().getNome())
                            .estado(partida.getClubeVisitante().getEstado())
                            .dataCriacao(partida.getClubeVisitante().getDataCriacao())
                            .ativo(partida.getClubeVisitante().isAtivo())
                            .build())
                    .golsClubeMandante(partida.getGolsClubeMandante())
                    .golsClubeVisitante(partida.getGolsClubeVisitante())
                    .estadio(
                            EstadioDTO.builder()
                                    .id(partida.getEstadio().getId())
                                    .nome(partida.getEstadio().getNome())
                                    .build()
                    )
                    .build();
        }).collect(Collectors.toList());

        String nomeAdversario = clube2.getNome();

        RetrospectoClubeDTO clubePesquisado = new RetrospectoClubeDTO(clube1.getNome(), vitorias, empates, derrotas, golsFeitos, golsSofridos);

        return new RetrospectoPartidasEntreClubesDTO(partidasDTO, nomeAdversario, clubePesquisado);
    }
}
