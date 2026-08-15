package com.API_de_Funcionarios.repository;

import com.API_de_Funcionarios.entity.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Page<Cargo> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

}

