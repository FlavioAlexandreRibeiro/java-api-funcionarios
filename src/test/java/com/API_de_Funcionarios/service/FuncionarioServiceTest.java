package com.API_de_Funcionarios.service;

import com.API_de_Funcionarios.dto.FuncionarioRequestDTO;
import com.API_de_Funcionarios.dto.FuncionarioResponseDTO;
import com.API_de_Funcionarios.entity.Cargo;
import com.API_de_Funcionarios.entity.Funcionario;
import com.API_de_Funcionarios.exception.CargoNaoEncontradoException;
import com.API_de_Funcionarios.exception.FuncionarioDuplicadoException;
import com.API_de_Funcionarios.exception.FuncionarioNaoEncontradoException;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario funcionario;
    private Cargo cargo;
    private FuncionarioRequestDTO funcionarioRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setNome("Desenvolvedor");
        cargo.setDescricao("Desenvolvedor de Software");

        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("João Silva");
        funcionario.setCpf("123.456.789-00");
        funcionario.setEmail("joao.silva@email.com");
        funcionario.setTelefone("11999999999");
        funcionario.setSalario(new BigDecimal("5000.00"));
        funcionario.setDataAdmissao(LocalDate.of(2024, 1, 1));
        funcionario.setCargo(cargo);

        funcionarioRequestDTO = new FuncionarioRequestDTO();
        funcionarioRequestDTO.setNome("João Silva");
        funcionarioRequestDTO.setCpf("123.456.789-00");
        funcionarioRequestDTO.setEmail("joao.silva@email.com");
        funcionarioRequestDTO.setTelefone("11999999999");
        funcionarioRequestDTO.setSalario(new BigDecimal("5000.00"));
        funcionarioRequestDTO.setDataAdmissao(LocalDate.of(2024, 1, 1));
        funcionarioRequestDTO.setCargoId(1L);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void cadastrar_DeveRetornarFuncionarioResponseDTO_QuandoDadosValidos() {
        when(funcionarioRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(funcionarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        FuncionarioResponseDTO result = funcionarioService.cadastrar(funcionarioRequestDTO);

        assertNotNull(result);
        assertEquals("João Silva", result.nome());
        assertEquals("123.456.789-00", result.cpf());
        assertEquals("Desenvolvedor", result.cargo());
        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCase("João Silva");
        verify(funcionarioRepository, times(1)).existsByCpf("123.456.789-00");
        verify(cargoRepository, times(1)).findById(1L);
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void cadastrar_DeveLancarFuncionarioDuplicadoException_QuandoNomeJaExiste() {
        when(funcionarioRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        assertThrows(FuncionarioDuplicadoException.class, () -> funcionarioService.cadastrar(funcionarioRequestDTO));

        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCase("João Silva");
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void cadastrar_DeveLancarFuncionarioDuplicadoException_QuandoCpfJaExiste() {
        when(funcionarioRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(funcionarioRepository.existsByCpf(anyString())).thenReturn(true);

        assertThrows(FuncionarioDuplicadoException.class, () -> funcionarioService.cadastrar(funcionarioRequestDTO));

        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCase("João Silva");
        verify(funcionarioRepository, times(1)).existsByCpf("123.456.789-00");
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void cadastrar_DeveLancarCargoNaoEncontradoException_QuandoCargoNaoExiste() {
        when(funcionarioRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(funcionarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNaoEncontradoException.class, () -> funcionarioService.cadastrar(funcionarioRequestDTO));

        verify(cargoRepository, times(1)).findById(1L);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void buscarPorId_DeveRetornarFuncionarioResponseDTO_QuandoIdExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        FuncionarioResponseDTO result = funcionarioService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("João Silva", result.nome());
        verify(funcionarioRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveLancarFuncionarioNaoEncontradoException_QuandoIdNaoExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FuncionarioNaoEncontradoException.class, () -> funcionarioService.buscarPorId(1L));

        verify(funcionarioRepository, times(1)).findById(1L);
    }

    @Test
    void listar_DeveRetornarPaginaDeFuncionarios_QuandoNomeNaoFornecido() {
        Page<Funcionario> page = new PageImpl<>(List.of(funcionario));
        when(funcionarioRepository.findAll(pageable)).thenReturn(page);

        Page<FuncionarioResponseDTO> result = funcionarioService.listar(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).nome());
        verify(funcionarioRepository, times(1)).findAll(pageable);
        verify(funcionarioRepository, never()).findByNomeContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void listar_DeveRetornarPaginaDeFuncionarios_QuandoNomeFornecido() {
        Page<Funcionario> page = new PageImpl<>(List.of(funcionario));
        when(funcionarioRepository.findByNomeContainingIgnoreCase("João", pageable)).thenReturn(page);

        Page<FuncionarioResponseDTO> result = funcionarioService.listar("João", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).nome());
        verify(funcionarioRepository, times(1)).findByNomeContainingIgnoreCase("João", pageable);
        verify(funcionarioRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void atualizar_DeveRetornarFuncionarioResponseDTO_QuandoDadosValidos() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.existsByNomeIgnoreCaseAndIdNot("Maria Silva", 1L)).thenReturn(false);
        when(funcionarioRepository.existsByCpfAndIdNot("987.654.321-00", 1L)).thenReturn(false);
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        FuncionarioRequestDTO novoDTO = new FuncionarioRequestDTO();
        novoDTO.setNome("Maria Silva");
        novoDTO.setCpf("987.654.321-00");
        novoDTO.setEmail("maria.silva@email.com");
        novoDTO.setTelefone("11888888888");
        novoDTO.setSalario(new BigDecimal("6000.00"));
        novoDTO.setDataAdmissao(LocalDate.of(2024, 2, 1));
        novoDTO.setCargoId(1L);

        FuncionarioResponseDTO result = funcionarioService.atualizar(1L, novoDTO);

        assertNotNull(result);
        verify(funcionarioRepository, times(1)).findById(1L);
        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCaseAndIdNot("Maria Silva", 1L);
        verify(funcionarioRepository, times(1)).existsByCpfAndIdNot("987.654.321-00", 1L);
        verify(cargoRepository, times(1)).findById(1L);
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void atualizar_DeveLancarFuncionarioNaoEncontradoException_QuandoIdNaoExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FuncionarioNaoEncontradoException.class, () -> funcionarioService.atualizar(1L, funcionarioRequestDTO));

        verify(funcionarioRepository, times(1)).findById(1L);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void atualizar_DeveLancarFuncionarioDuplicadoException_QuandoNomeJaExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.existsByNomeIgnoreCaseAndIdNot("João Silva", 1L)).thenReturn(true);

        assertThrows(FuncionarioDuplicadoException.class, () -> funcionarioService.atualizar(1L, funcionarioRequestDTO));

        verify(funcionarioRepository, times(1)).findById(1L);
        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCaseAndIdNot("João Silva", 1L);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void atualizar_DeveLancarFuncionarioDuplicadoException_QuandoCpfJaExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.existsByNomeIgnoreCaseAndIdNot("João Silva", 1L)).thenReturn(false);
        when(funcionarioRepository.existsByCpfAndIdNot("123.456.789-00", 1L)).thenReturn(true);

        assertThrows(FuncionarioDuplicadoException.class, () -> funcionarioService.atualizar(1L, funcionarioRequestDTO));

        verify(funcionarioRepository, times(1)).findById(1L);
        verify(funcionarioRepository, times(1)).existsByNomeIgnoreCaseAndIdNot("João Silva", 1L);
        verify(funcionarioRepository, times(1)).existsByCpfAndIdNot("123.456.789-00", 1L);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void atualizar_DeveLancarCargoNaoEncontradoException_QuandoCargoNaoExiste() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.existsByNomeIgnoreCaseAndIdNot("João Silva", 1L)).thenReturn(false);
        when(funcionarioRepository.existsByCpfAndIdNot("123.456.789-00", 1L)).thenReturn(false);
        when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNaoEncontradoException.class, () -> funcionarioService.atualizar(1L, funcionarioRequestDTO));

        verify(cargoRepository, times(1)).findById(1L);
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void deletar_DeveDeletarFuncionario_QuandoIdExiste() {
        when(funcionarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(funcionarioRepository).deleteById(1L);

        funcionarioService.deletar(1L);

        verify(funcionarioRepository, times(1)).existsById(1L);
        verify(funcionarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_DeveLancarFuncionarioNaoEncontradoException_QuandoIdNaoExiste() {
        when(funcionarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(FuncionarioNaoEncontradoException.class, () -> funcionarioService.deletar(1L));

        verify(funcionarioRepository, times(1)).existsById(1L);
        verify(funcionarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void buscarPorCpf_DeveRetornarFuncionarioResponseDTO_QuandoCpfExiste() {
        when(funcionarioRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(funcionario));

        FuncionarioResponseDTO result = funcionarioService.buscarPorCpf("123.456.789-00");

        assertNotNull(result);
        assertEquals("123.456.789-00", result.cpf());
        assertEquals("João Silva", result.nome());
        verify(funcionarioRepository, times(1)).findByCpf("123.456.789-00");
    }

    @Test
    void buscarPorCpf_DeveLancarFuncionarioNaoEncontradoException_QuandoCpfNaoExiste() {
        when(funcionarioRepository.findByCpf("123.456.789-00")).thenReturn(Optional.empty());

        assertThrows(FuncionarioNaoEncontradoException.class, () -> funcionarioService.buscarPorCpf("123.456.789-00"));

        verify(funcionarioRepository, times(1)).findByCpf("123.456.789-00");
    }
}
