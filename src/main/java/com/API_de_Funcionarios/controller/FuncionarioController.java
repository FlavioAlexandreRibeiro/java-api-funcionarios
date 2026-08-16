package com.API_de_Funcionarios.controller;

import com.API_de_Funcionarios.dto.FuncionarioRequestDTO;
import com.API_de_Funcionarios.dto.FuncionarioResponseDTO;
import com.API_de_Funcionarios.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionario")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criar(
            @Valid @RequestBody FuncionarioRequestDTO funcionario) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(funcionarioService.cadastrar(funcionario));
    }


    @GetMapping
    public Page<FuncionarioResponseDTO> listar(
            @RequestParam(required = false) String nome,
            Pageable pageable) {

        return funcionarioService.listar(nome, pageable);
    }


    @GetMapping("/{id}")
    public FuncionarioResponseDTO buscarPorId(@PathVariable Long id) {
        return funcionarioService.buscarPorId(id);
    }


    @GetMapping("/cpf")
    public FuncionarioResponseDTO buscarPorCpf(@RequestParam String cpf) {
        return funcionarioService.buscarPorCpf(cpf);
    }


    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequestDTO funcionario) {

        return ResponseEntity.ok(funcionarioService.atualizar(id, funcionario));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
