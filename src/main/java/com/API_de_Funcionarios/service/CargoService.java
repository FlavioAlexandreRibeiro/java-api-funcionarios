package com.API_de_Funcionarios.service;

import com.API_de_Funcionarios.dto.CargoRequestDTO;
import com.API_de_Funcionarios.dto.CargoResponseDTO;
import com.API_de_Funcionarios.entity.Cargo;
import com.API_de_Funcionarios.exception.CargoDuplicadoException;
import com.API_de_Funcionarios.exception.CargoEmUsoException;
import com.API_de_Funcionarios.exception.CargoNaoEncontradoException;
import com.API_de_Funcionarios.repository.CargoRepository;
import com.API_de_Funcionarios.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final FuncionarioRepository funcionarioRepository;


    public CargoResponseDTO cadastrar(CargoRequestDTO cargoDTO) {

        if (cargoRepository.existsByNomeIgnoreCase(
                cargoDTO.getNome())) {
            throw new CargoDuplicadoException(
                    "Cargo com nome '" + cargoDTO.getNome() + "' já existe");
        }

        Cargo cargo = new Cargo();

        cargo.setNome(cargoDTO.getNome());
        cargo.setDescricao(cargoDTO.getDescricao());

        Cargo cargoSalvo = cargoRepository.save(cargo);

        return CargoResponseDTO.mapearParaDto(cargoSalvo);
    }


    public CargoResponseDTO buscarPorId(Long id) {

        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new CargoNaoEncontradoException(
                        "Cargo com ID " + id + " não encontrado."));

        return CargoResponseDTO.mapearParaDto(cargo);
    }


    public Page<CargoResponseDTO> listar(
            String nome,
            Pageable pageable) {

        if (nome != null && !nome.isBlank()) {

            return cargoRepository
                    .findByNomeContainingIgnoreCase(nome, pageable)
                    .map(CargoResponseDTO::mapearParaDto);
        }

        return cargoRepository.findAll(pageable)
                .map(CargoResponseDTO::mapearParaDto);
    }


    public CargoResponseDTO atualizar(Long id, CargoRequestDTO cargoDTO) {

        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new CargoNaoEncontradoException(
                        "Cargo não encontrado."));

        if (cargoRepository.existsByNomeIgnoreCaseAndIdNot(
                cargoDTO.getNome(), id)) {

            throw new CargoDuplicadoException(
                    "Cargo com nome '" + cargoDTO.getNome() + "' já existe.");
        }

        cargo.setNome(cargoDTO.getNome());
        cargo.setDescricao(cargoDTO.getDescricao());

        Cargo cargoAtualizado = cargoRepository.save(cargo);

        return CargoResponseDTO.mapearParaDto(cargoAtualizado);
    }


    public void deletar(Long id) {

        if (!cargoRepository.existsById(id)) {
            throw new CargoNaoEncontradoException(
                    "Cargo com ID " + id + " não encontrado");
        }

        if (funcionarioRepository.existsByCargoId(id)) {
            throw new CargoEmUsoException(
                    "Não é possível excluir o cargo porque existem funcionários associados.");
        }
        cargoRepository.deleteById(id);
    }
}

