package com.API_de_Funcionarios.service;

import com.API_de_Funcionarios.dto.FuncionarioRequestDTO;
import com.API_de_Funcionarios.dto.FuncionarioResponseDTO;
import com.API_de_Funcionarios.entity.Cargo;
import com.API_de_Funcionarios.entity.Funcionario;
import com.API_de_Funcionarios.repository.CargoRepository;
import com.API_de_Funcionarios.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository;


    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO funcionarioDTO) {

        if (funcionarioRepository.existsByNomeIgnoreCase(
                funcionarioDTO.getNome())) {
            throw new FuncionarioDuplicadoException(
                    "Funcionário com nome '" + funcionarioDTO.getNome() + "' já existe");
        }

        if (funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())) {
            throw new FuncionarioDuplicadoException(
                    "Funcionário com CPF '" + funcionarioDTO.getCpf() + "' já existe.");
        }

        Cargo cargo = cargoRepository.findById(funcionarioDTO.getCargoId())
                .orElseThrow(() -> new CargoNaoEncontradoException(
                        "Cargo com ID " + funcionarioDTO.getCargoId() + " não encontrado."));

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setCpf(funcionarioDTO.getCpf());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setTelefone(funcionarioDTO.getTelefone());
        funcionario.setSalario(funcionarioDTO.getSalario());
        funcionario.setDataAdmissao(funcionarioDTO.getDataAdmissao());
        funcionario.setCargo(cargo);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        return FuncionarioResponseDTO.mapearParaDto(funcionarioSalvo);

    }


    public FuncionarioResponseDTO buscarPorId(Long id) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(
                        "Funcionário com ID " + id + " não encontrado."));

        return FuncionarioResponseDTO.mapearParaDto(funcionario);
    }


    public Page<FuncionarioResponseDTO> listar(
            String nome,
            Pageable pageable) {

        if (nome != null && !nome.isBlank()) {

            return funcionarioRepository
                    .findByNomeContainingIgnoreCase(nome, pageable)
                    .map(FuncionarioResponseDTO::mapearParaDto);
        }

        return funcionarioRepository.findAll(pageable)
                .map(FuncionarioResponseDTO::mapearParaDto);
    }


    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO funcionarioDTO) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(
                        "Funcionário não encontrado."));

        if (funcionarioRepository.existsByNomeIgnoreCaseAndIdNot(
                funcionarioDTO.getNome(), id)) {

            throw new FuncionarioDuplicadoException(
                    "Funcionário com nome'" + funcionarioDTO.getNome() + "' já existe.");
        }

        if (funcionarioRepository.existsByCpfAndIdNot(
                funcionarioDTO.getCpf(), id)) {

            throw new FuncionarioDuplicadoException(
                    "Funcionário com CPF '" + funcionarioDTO.getCpf() + "' já existe.");
        }

        Cargo cargo = cargoRepository.findById(funcionarioDTO.getCargoId())
                .orElseThrow(() -> new CargoNaoEncontradoException(
                        "Cargo com ID " + funcionarioDTO.getCargoId() + " não encontrado."));


        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setCpf(funcionarioDTO.getCpf());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setTelefone(funcionarioDTO.getTelefone());
        funcionario.setSalario(funcionarioDTO.getSalario());
        funcionario.setDataAdmissao(funcionarioDTO.getDataAdmissao());
        funcionario.setCargo(cargo);

        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);

        return FuncionarioResponseDTO.mapearParaDto(funcionarioAtualizado);
    }


    public void deletar(Long id) {

        if (!funcionarioRepository.existsById(id)) {
            throw new FuncionarioNaoEncontradoException(
                    "Funcionário com ID " + id + " não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }


    public FuncionarioResponseDTO buscarPorCpf(String cpf) {

        Funcionario funcionario = funcionarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(
                        "Funcionário com CPF '" + cpf + "' não encontrado"));

        return FuncionarioResponseDTO.mapearParaDto(funcionario);
    }
}