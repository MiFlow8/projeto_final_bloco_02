package com.generation.farmacia.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	// 1. Buscar todas
	@GetMapping
	public ResponseEntity<List<Categoria>> getAll() {
	    return ResponseEntity.ok(categoriaRepository.findAll());
	}

	// 2. Buscar por ID
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id) {
	    return categoriaRepository.findById(id)
	            .map(resposta -> ResponseEntity.ok(resposta))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// 3. Buscar por descrição 
	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<Object> getByDescricao(@PathVariable String descricao) {
	    return ResponseEntity.ok(
	        categoriaRepository.findAllByDescricaoContainingIgnoreCase(descricao)
	    );
	}

	// 4. Cadastrar
	@PostMapping
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(categoriaRepository.save(categoria));
	}

	// 5. Atualizar
	@PutMapping
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
	    return categoriaRepository.findById(categoria.getId())
	            .map(resposta -> ResponseEntity.status(HttpStatus.OK)
	                    .body(categoriaRepository.save(categoria)))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// 6. Deletar
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
	    return categoriaRepository.findById(id)
	            .map(categoria -> {
	                categoriaRepository.deleteById(id);
	                return ResponseEntity.noContent().build();
	            })
	            .orElse(ResponseEntity.notFound().build());
	}
}