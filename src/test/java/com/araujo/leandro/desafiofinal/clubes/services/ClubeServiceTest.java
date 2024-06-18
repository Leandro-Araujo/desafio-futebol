package com.araujo.leandro.desafiofinal.clubes.services;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.clubes.repositories.ClubeRepository;
import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public class ClubeServiceTest {
    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeService clubeService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveCadastrarClubeComSucesso() {
        Mockito.when(clubeRepository.findByNomeAndEstado("Flamengo", "MA")).thenReturn(Optional.empty());
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome("Flamengo");
        clubeDTO.setEstado("MA");
        clubeDTO.setDataCriacao(new Date("1895/11/17"));
        clubeDTO.setAtivo(true);

        clubeService.cadastrarClube(clubeDTO);
        Mockito.verify(clubeRepository, Mockito.times(1)).save(Mockito.any());
    }
    
    @Test
    public void deveEditarClube() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Flamengo");
        clube.setEstado("RJ");
        clube.setDataCriacao(new Date("1895/11/17"));
        clube.setAtivo(true);
        Mockito.when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        Mockito.when(clubeRepository.findByNomeAndEstado("Flamengo", "MA")).thenReturn(Optional.empty());
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome("Flamengo");
        clubeDTO.setEstado("MA");
        clubeDTO.setDataCriacao(new Date("1895/11/17"));
        clubeDTO.setAtivo(true);

        clubeService.editarClube(1L, clubeDTO);
        Mockito.verify(clubeRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void deveInativarClube() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Flamengo");
        clube.setEstado("RJ");
        clube.setDataCriacao(new Date("1895/11/17"));
        clube.setAtivo(true);
        Mockito.when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        clubeService.inativarClube(1L);
        Assert.assertFalse(clube.isAtivo());
        Mockito.verify(clubeRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void deveBuscarClubePeloId() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Flamengo");
        clube.setEstado("RJ");
        clube.setDataCriacao(new Date("1895/11/17"));
        clube.setAtivo(true);
        Mockito.when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        ClubeDTO clubeDTO = clubeService.buscarClube(1L);
        Assert.assertEquals(clube.getId(), clubeDTO.getId());
        Assert.assertEquals(clube.getNome(), clubeDTO.getNome());
        Assert.assertEquals(clube.getEstado(), clubeDTO.getEstado());
        Assert.assertEquals(clube.getDataCriacao(), clubeDTO.getDataCriacao());
        Assert.assertEquals(clube.isAtivo(), clubeDTO.isAtivo());
    }
}
