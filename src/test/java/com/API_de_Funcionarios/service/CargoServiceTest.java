package com.API_de_Funcionarios.service;

import com.API_de_Funcionarios.dto.CargoRequestDTO;
import com.API_de_Funcionarios.dto.CargoResponseDTO;
import com.API_de_Funcionarios.entity.Cargo;
import com.API_de_Funcionarios.exception.CargoDuplicadoException;
import com.API_de_Funcionarios.exception.CargoEmUsoException;
import com.API_de_Funcionarios.exception.CargoNaoEncontradoException;
import com.API_de_Funcionarios.repository.CargoRepository;
import com.API_de_Funcionarios.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private CargoService cargoService;

    private Cargo cargo;
    private CargoRequestDTO cargoRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setNome("Desenvolvedor");
        cargo.setDescricao("Desenvolvedor de Software");

        cargoRequestDTO = new CargoRequestDTO();
        cargoRequestDTO.setNome("Desenvolvedor");
        cargoRequestDTO.setDescricao("Desenvolvedor de Software");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void cadastrar_DeveRetornarCargoResponseDTO_QuandoNomeNaoExiste() {
        when(cargoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        CargoResponseDTO result = cargoService.cadastrar(cargoRequestDTO);

        assertNotNull(result);
        assertEquals("Desenvolvedor", result.nome());
        assertEquals("Desenvolvedor de Software", result.descricao());
        verify(cargoRepository, times(1)).existsByNomeIgnoreCase("Desenvolvedor");
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void cadastrar_DeveLancarCargoDuplicadoException_QuandoNomeJaExiste() {
        when(cargoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        assertThrows(CargoDuplicadoException.class, () -> cargoService.cadastrar(cargoRequestDTO));

        verify(cargoRepository, times(1)).existsByNomeIgnoreCase("Desenvolvedor");
        verify(cargoRepository, never()).save(any(Cargo.class));
    }

    @Test
    void buscarPorId_DeveRetornarCargoResponseDTO_QuandoIdExiste() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));

        CargoResponseDTO result = cargoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Desenvolvedor", result.nome());
        verify(cargoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveLancarCargoNaoEncontradoException_QuandoIdNaoExiste() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNaoEncontradoException.class, () -> cargoService.buscarPorId(1L));

        verify(cargoRepository, times(1)).findById(1L);
    }

    @Test
    void listar_DeveRetornarPaginaDeCargos_QuandoNomeNaoFornecido() {
        Page<Cargo> page = new PageImpl<>(List.of(cargo));
        when(cargoRepository.findAll(pageable)).thenReturn(page);

        Page<CargoResponseDTO> result = cargoService.listar(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Desenvolvedor", result.getContent().get(0).nome());
        verify(cargoRepository, times(1)).findAll(pageable);
        verify(cargoRepository, never()).findByNomeContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void listar_DeveRetornarPaginaDeCargos_QuandoNomeFornecido() {
        Page<Cargo> page = new PageImpl<>(List.of(cargo));
        when(cargoRepository.findByNomeContainingIgnoreCase("Desenvolvedor", pageable)).thenReturn(page);

        Page<CargoResponseDTO> result = cargoService.listar("Desenvolvedor", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Desenvolvedor", result.getContent().get(0).nome());
        verify(cargoRepository, times(1)).findByNomeContainingIgnoreCase("Desenvolvedor", pageable);
        verify(cargoRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void atualizar_DeveRetornarCargoResponseDTO_QuandoDadosValidos() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(cargoRepository.existsByNomeIgnoreCaseAndIdNot("Desenvolvedor Senior", 1L)).thenReturn(false);
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        CargoRequestDTO novoDTO = new CargoRequestDTO();
        novoDTO.setNome("Desenvolvedor Senior");
        novoDTO.setDescricao("Desenvolvedor Senior de Software");

        CargoResponseDTO result = cargoService.atualizar(1L, novoDTO);

        assertNotNull(result);
        verify(cargoRepository, times(1)).findById(1L);
        verify(cargoRepository, times(1)).existsByNomeIgnoreCaseAndIdNot("Desenvolvedor Senior", 1L);
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void atualizar_DeveLancarCargoNaoEncontradoException_QuandoIdNaoExiste() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNaoEncontradoException.class, () -> cargoService.atualizar(1L, cargoRequestDTO));

        verify(cargoRepository, times(1)).findById(1L);
        verify(cargoRepository, never()).save(any(Cargo.class));
    }

    @Test
    void atualizar_DeveLancarCargoDuplicadoException_QuandoNomeJaExiste() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(cargoRepository.existsByNomeIgnoreCaseAndIdNot("Desenvolvedor", 1L)).thenReturn(true);

        assertThrows(CargoDuplicadoException.class, () -> cargoService.atualizar(1L, cargoRequestDTO));

        verify(cargoRepository, times(1)).findById(1L);
        verify(cargoRepository, times(1)).existsByNomeIgnoreCaseAndIdNot("Desenvolvedor", 1L);
        verify(cargoRepository, never()).save(any(Cargo.class));
    }

    @Test
    void deletar_DeveDeletarCargo_QuandoCargoExisteENaoEstaEmUso() {
        when(cargoRepository.existsById(1L)).thenReturn(true);
        when(funcionarioRepository.existsByCargoId(1L)).thenReturn(false);
        doNothing().when(cargoRepository).deleteById(1L);

        cargoService.deletar(1L);

        verify(cargoRepository, times(1)).existsById(1L);
        verify(funcionarioRepository, times(1)).existsByCargoId(1L);
        verify(cargoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_DeveLancarCargoNaoEncontradoException_QuandoIdNaoExiste() {
        when(cargoRepository.existsById(1L)).thenReturn(false);

        assertThrows(CargoNaoEncontradoException.class, () -> cargoService.deletar(1L));

        verify(cargoRepository, times(1)).existsById(1L);
        verify(cargoRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletar_DeveLancarCargoEmUsoException_QuandoCargoEstaEmUso() {
        when(cargoRepository.existsById(1L)).thenReturn(true);
        when(funcionarioRepository.existsByCargoId(1L)).thenReturn(true);

        assertThrows(CargoEmUsoException.class, () -> cargoService.deletar(1L));

        verify(cargoRepository, times(1)).existsById(1L);
        verify(funcionarioRepository, times(1)).existsByCargoId(1L);
        verify(cargoRepository, never()).deleteById(anyLong());
    }
}
