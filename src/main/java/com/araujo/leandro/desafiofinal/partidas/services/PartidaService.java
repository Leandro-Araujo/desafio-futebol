package com.araujo.leandro.desafiofinal.partidas.services;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.clubes.repositories.ClubeRepository;
import com.araujo.leandro.desafiofinal.estadios.dtos.EstadioDTO;
import com.araujo.leandro.desafiofinal.estadios.models.Estadio;
import com.araujo.leandro.desafiofinal.estadios.repositories.EstadioRepository;
import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaDTO;
import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaResponseDTO;
import com.araujo.leandro.desafiofinal.partidas.models.Partida;
import com.araujo.leandro.desafiofinal.partidas.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class PartidaService {
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    @Autowired
    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }

    public void cadastrarPartida(PartidaDTO partidaDTO){
        Clube clubeMandante = clubeRepository
                .findById(partidaDTO.getClubeMandanteId())
                .orElseThrow(() -> new IllegalArgumentException("Clube mandante não encontrado"));
        Clube clubeVisitante = clubeRepository
                .findById(partidaDTO.getClubeVisitanteId())
                .orElseThrow(() -> new IllegalArgumentException("Clube visitante não encontrado"));

        validaClubes(partidaDTO, clubeMandante, clubeVisitante);

        Estadio estadio = buscaEstadio(partidaDTO);

        Partida partida = new Partida(
                clubeMandante,
                clubeVisitante,
                partidaDTO.getGolsClubeMandante(),
                partidaDTO.getGolsClubeVisitante(),
                estadio,
                partidaDTO.getDataHora()
        );

        partidaRepository.save(partida);
    }
    private void validaClubes(PartidaDTO partidaDTO, Clube clubeMandante, Clube clubeVisitante){
        if (partidaDTO.getDataHora().isBefore(clubeMandante.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) ||
                partidaDTO.getDataHora().isBefore(clubeVisitante.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        ) {
            throw new ConflitoDadosException("Data da partida não pode ser anterior à data de criação dos clubes");
        }
        if (!clubeMandante.isAtivo() || !clubeVisitante.isAtivo()) {
            throw new ConflitoDadosException("Clubes devem estar ativos para jogar");
        }

        List<Partida> partidas = partidaRepository.findAll();

        List<Partida> partidasMandante = partidas.stream()
                .filter(partida -> partida.getClubeMandante().equals(clubeMandante) || partida.getClubeVisitante().equals(clubeMandante))
                .filter(partida -> partida.getDataHora().isAfter(partidaDTO.getDataHora().minusHours(48)) && partida.getDataHora().isBefore(partidaDTO.getDataHora().plusHours(48)))
                .toList();

        if (!partidasMandante.isEmpty()) {
            throw new ConflitoDadosException("Clubes já possuem partida marcada com diferença menor do que 48 horas");
        }

        List<Partida> partidasVisitante = partidas.stream()
                .filter(partida -> partida.getClubeMandante().equals(clubeVisitante) || partida.getClubeVisitante().equals(clubeVisitante))
                .filter(partida -> partida.getDataHora().isAfter(partidaDTO.getDataHora().minusHours(48)) && partida.getDataHora().isBefore(partidaDTO.getDataHora().plusHours(48)))
                .toList();

        if (!partidasVisitante.isEmpty()) {
            throw new ConflitoDadosException("Clubes já possuem partida marcada com diferença menor do que 48 horas");
        }
    }
    private Estadio buscaEstadio(PartidaDTO partidaDTO){
        Estadio estadio = estadioRepository
                .findById(partidaDTO.getEstadioId())
                .orElseThrow(() -> new IllegalArgumentException("Estádio não encontrado"));

        if (partidaRepository
                .existsByEstadioAndDataHoraBetween(estadio,
                        partidaDTO.getDataHora().toLocalDate().atStartOfDay(),
                        partidaDTO.getDataHora().toLocalDate().atTime(23, 59))
        ) {
            throw new ConflitoDadosException("Estádio já possui jogo marcado para o mesmo dia");
        }
        return estadio;
    }
    private Estadio buscaEstadioPut(PartidaDTO partidaDTO){
        Estadio estadio = estadioRepository
                .findById(partidaDTO.getEstadioId())
                .orElseThrow(() -> new IllegalArgumentException("Estádio não encontrado"));
        List<Partida> partidas = partidaRepository.findByEstadio(estadio).stream().filter(partida -> !partida.getId().equals(partidaDTO.getId())).toList();
        if (partidas.stream()
                .anyMatch(partida -> partida.getDataHora().toLocalDate().equals(partidaDTO.getDataHora().toLocalDate()))
        ) {
            throw new ConflitoDadosException("Estádio já possui jogo marcado para o mesmo dia");
        }
        return estadio;
    }
    public void editarPartida(Long id, PartidaDTO partidaDTO){
        Partida partida = partidaRepository
                .findById(id)
                .orElseThrow(() -> new DadoNaoExiste("Partida não encontrada"));
        Clube clubeMandante = clubeRepository
                .findById(partidaDTO.getClubeMandanteId())
                .orElseThrow(() -> new IllegalArgumentException("Clube mandante não encontrado"));
        Clube clubeVisitante = clubeRepository
                .findById(partidaDTO.getClubeVisitanteId())
                .orElseThrow(() -> new IllegalArgumentException("Clube visitante não encontrado"));
        validaClubes(id, partidaDTO, clubeMandante, clubeVisitante);

        Estadio estadio = buscaEstadioPut(partidaDTO);

        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setGolsClubeMandante(partidaDTO.getGolsClubeMandante());
        partida.setGolsClubeVisitante(partidaDTO.getGolsClubeVisitante());
        partida.setEstadio(estadio);
        partida.setDataHora(partidaDTO.getDataHora());

        partidaRepository.save(partida);
    }
    private void validaClubes(Long id, PartidaDTO partidaDTO, Clube clubeMandante, Clube clubeVisitante){
        if (partidaDTO.getDataHora().isBefore(clubeMandante.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) ||
                partidaDTO.getDataHora().isBefore(clubeVisitante.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        ) {
            throw new ConflitoDadosException("Data da partida não pode ser anterior à data de criação dos clubes");
        }
        if (!clubeMandante.isAtivo() || !clubeVisitante.isAtivo()) {
            throw new ConflitoDadosException("Clubes devem estar ativos para jogar");
        }
        List<Partida> partidas = partidaRepository.findAll();
        List<Partida> partidasMandante = partidas.stream()
                .filter(partida -> !partida.getId().equals(id))
                .filter(partida -> partida.getClubeMandante().equals(clubeMandante) || partida.getClubeVisitante().equals(clubeMandante))
                .filter(partida -> partida.getDataHora().isAfter(partidaDTO.getDataHora().minusHours(48)) && partida.getDataHora().isBefore(partidaDTO.getDataHora().plusHours(48)))
                .toList();
        if (!partidasMandante.isEmpty()) {
            throw new ConflitoDadosException("Clubes já possuem partida marcada com diferença menor do que 48 horas");
        }
        List<Partida> partidasVisitante = partidas.stream()
                .filter(partida -> !partida.getId().equals(id))
                .filter(partida -> partida.getClubeMandante().equals(clubeVisitante) || partida.getClubeVisitante().equals(clubeVisitante))
                .filter(partida -> partida.getDataHora().isAfter(partidaDTO.getDataHora().minusHours(48)) && partida.getDataHora().isBefore(partidaDTO.getDataHora().plusHours(48)))
                .toList();
        if (!partidasVisitante.isEmpty()) {
            throw new ConflitoDadosException("Clubes já possuem partida marcada com diferença menor do que 48 horas");
        }
    }
    public void removerPartida(Long id){
        Partida partida = partidaRepository
                .findById(id)
                .orElseThrow(() -> new DadoNaoExiste("Partida não encontrada"));
        partidaRepository.delete(partida);
    }
    public PartidaResponseDTO buscarPartida(Long id){
        Partida partida = partidaRepository
                .findById(id)
                .orElseThrow(() -> new DadoNaoExiste("Partida não encontrada"));

        ClubeDTO clubeMandante = ClubeDTO.builder()
                .id(partida.getClubeMandante().getId())
                .nome(partida.getClubeMandante().getNome())
                .dataCriacao(partida.getClubeMandante().getDataCriacao())
                .ativo(partida.getClubeMandante().isAtivo())
                .build();

        ClubeDTO clubeVisitante = ClubeDTO.builder()
                .id(partida.getClubeVisitante().getId())
                .nome(partida.getClubeVisitante().getNome())
                .dataCriacao(partida.getClubeVisitante().getDataCriacao())
                .ativo(partida.getClubeVisitante().isAtivo())
                .build();

        EstadioDTO estadio = EstadioDTO.builder()
                .id(partida.getEstadio().getId())
                .nome(partida.getEstadio().getNome())
                .build();

        return PartidaResponseDTO.builder()
                .id(partida.getId())
                .clubeMandante(clubeMandante)
                .clubeVisitante(clubeVisitante)
                .golsClubeMandante(partida.getGolsClubeMandante())
                .golsClubeVisitante(partida.getGolsClubeVisitante())
                .estadio(estadio)
                .build();
    }
    public Page<PartidaResponseDTO> listarPartidas(String nomeClube, String nomeEstadio, Integer page, Integer tamanhoPagina, String ordenacao, String direcao){
        Pageable pageable = PageRequest.of(page, tamanhoPagina, direcao.equals("asc") ? Sort.by(ordenacao).ascending() : Sort.by(ordenacao).descending());

        Page<Partida> partidas;
        if(nomeClube != null && nomeEstadio != null){
            partidas = partidaRepository.findByClubeMandanteNomeContainingOrClubeVisitanteNomeContainingAndEstadioNomeContaining(nomeClube, nomeClube, nomeEstadio, pageable);
        } else if(nomeClube != null){
            partidas = partidaRepository.findByClubeMandanteNomeContainingOrClubeVisitanteNomeContaining(nomeClube, nomeClube, pageable);
        } else if(nomeEstadio != null){
            partidas = partidaRepository.findByEstadioNomeContaining(nomeEstadio, pageable);
        } else {
            partidas = partidaRepository.findAll(pageable);
        }

        return partidas.map(partida -> {
            ClubeDTO clubeMandante = ClubeDTO.builder()
                    .id(partida.getClubeMandante().getId())
                    .nome(partida.getClubeMandante().getNome())
                    .dataCriacao(partida.getClubeMandante().getDataCriacao())
                    .ativo(partida.getClubeMandante().isAtivo())
                    .build();

            ClubeDTO clubeVisitante = ClubeDTO.builder()
                    .id(partida.getClubeVisitante().getId())
                    .nome(partida.getClubeVisitante().getNome())
                    .dataCriacao(partida.getClubeVisitante().getDataCriacao())
                    .ativo(partida.getClubeVisitante().isAtivo())
                    .build();

            EstadioDTO estadio = EstadioDTO.builder()
                    .id(partida.getEstadio().getId())
                    .nome(partida.getEstadio().getNome())
                    .build();

            return PartidaResponseDTO.builder()
                    .id(partida.getId())
                    .clubeMandante(clubeMandante)
                    .clubeVisitante(clubeVisitante)
                    .golsClubeMandante(partida.getGolsClubeMandante())
                    .golsClubeVisitante(partida.getGolsClubeVisitante())
                    .estadio(estadio)
                    .build();
        });
    }
}
