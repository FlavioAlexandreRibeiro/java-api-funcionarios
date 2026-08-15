package com.API_de_Funcionarios.repository;

import com.API_de_Funcionarios.entity.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    Optional<Funcionario> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByCargoId(Long cargoId);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Page<Funcionario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
