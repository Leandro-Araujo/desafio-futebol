package com.araujo.leandro.desafiofinal.estadios.services;

import com.araujo.leandro.desafiofinal.estadios.dtos.EstadioDTO;
import com.araujo.leandro.desafiofinal.estadios.models.Estadio;
import com.araujo.leandro.desafiofinal.estadios.repositories.EstadioRepository;
import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadioService {
    private final EstadioRepository estadioRepository;

    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public void cadastrarEstadio(EstadioDTO estadioDTO) {
        Estadio estadioMesmoNome = estadioRepository.findByNome(estadioDTO.getNome());
        if(estadioMesmoNome != null) {
            throw new ConflitoDadosException("Estádio já cadastrado");
        }
        Estadio estadio = new Estadio(estadioDTO.getNome());
        estadioRepository.save(estadio);
    }

    public void atualizarEstadio(Long id, EstadioDTO estadioDTO){
        Estadio estadio = estadioRepository.findById(id).orElseThrow( () -> new DadoNaoExiste("Estádio não encontrado"));
        estadio.setNome(estadioDTO.getNome());
        estadioRepository.save(estadio);
    }

    public EstadioDTO buscarEstadio(Long id){
        Estadio estadio = estadioRepository.findById(id).orElseThrow( () -> new DadoNaoExiste("Estádio não encontrado"));
        EstadioDTO estadioDTO = EstadioDTO.builder().nome(estadio.getNome()).build();
        return estadioDTO;
    }

    public List<EstadioDTO> listarEstadios(Integer pagina, Integer tamanhoPagina, String ordenacao, String direcao){
        Pageable paginacao = PageRequest.of(pagina, tamanhoPagina, Sort.by(Sort.Direction.fromString(direcao), ordenacao));
        Page<Estadio> estadios = estadioRepository.findAll(paginacao);
        return estadios.stream().map(estadio -> new EstadioDTO(estadio.getId(), estadio.getNome())).collect(Collectors.toList());
    }
}
