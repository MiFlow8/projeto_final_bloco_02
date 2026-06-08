package com.generation.farmacia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // 👇 INJEÇÃO QUE O PROFESSOR PEDIU
    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1 - GET ALL
    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    // 2 - GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(resp -> ResponseEntity.ok(resp))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3 - POST
    @PostMapping
    public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {

        if (!categoriaRepository.existsById(produto.getCategoria().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoRepository.save(produto));
    }

    // 4 - PUT
    @PutMapping
    public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
        return produtoRepository.findById(produto.getId())
                .map(resp -> ResponseEntity.ok(produtoRepository.save(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 5 - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(resp -> {
                    produtoRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}