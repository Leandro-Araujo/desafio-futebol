package com.araujo.leandro.desafiofinal.clubes.services;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.clubes.repositories.ClubeRepository;
import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }


    public void cadastrarClube(ClubeDTO clubeDTO){
        Optional<Clube> clubeMesmoNome = clubeRepository.findByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado());
        if(!clubeMesmoNome.isEmpty()){
            throw new ConflitoDadosException("Clube já cadastrado!");
        }
        Clube clube = new Clube(clubeDTO.getNome(),
                clubeDTO.getEstado(),
                clubeDTO.getDataCriacao(),
                clubeDTO.isAtivo());
        clubeRepository.save(clube);
    }

    public void editarClube(Long id, ClubeDTO clubeDTO){

        Clube clube = clubeRepository.findById(id).orElseThrow( ()-> new DadoNaoExiste("Dado nao existe")  );

        Optional<Clube> clubeMesmoNome = clubeRepository.findByNomeAndEstado(clubeDTO.getNome(), clubeDTO.getEstado());

        if(!clubeMesmoNome.isEmpty() && clubeMesmoNome.get().getId() != id){
            throw new ConflitoDadosException("Clube já cadastrado!");
        }
        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setDataCriacao(clubeDTO.getDataCriacao());
        clube.setAtivo(clubeDTO.isAtivo());

        clubeRepository.save(clube);
    }

    public void inativarClube(Long id){
        Clube clube = clubeRepository.findById(id).orElseThrow( ()-> new DadoNaoExiste("Dado nao existe")  );
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public ClubeDTO buscarClube(Long id){
        Clube clube = clubeRepository.findById(id).orElseThrow( ()-> new DadoNaoExiste("Dado nao existe")  );
        return new ClubeDTO(clube.getId(), clube.getNome(), clube.getEstado(), clube.getDataCriacao(), clube.isAtivo());
    }

    public Page<ClubeDTO> listarClubes(String nome, String estado, Boolean ativo, int pagina, int tamanhoPagina, String ordenacao, String direcao){
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, direcao.equals("asc") ? Sort.by(ordenacao).ascending() : Sort.by(ordenacao).descending());
        Page<Clube> pageClube;
        if (ativo != null){
            pageClube = clubeRepository.findByNomeContainingAndEstadoContainingAndAtivo(nome, estado, ativo, pageable);
        }
        else {
            pageClube = clubeRepository.findByNomeContainingAndEstadoContaining(nome, estado, pageable);
        }
        return pageClube.map(clube -> new ClubeDTO(clube.getId(), clube.getNome(), clube.getEstado(), clube.getDataCriacao(), clube.isAtivo()));
    }
}
