package br.com.fiap.financaspro.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.financaspro.model.Categoria;
import br.com.fiap.financaspro.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("categoria")
@CacheConfig(cacheNames = "categorias")
@Slf4j
@Tag(name = "categorias", description = "Categorias de Movimentações")
public class CategoriaController {

    @Autowired
    CategoriaRepository repository;

    
    @GetMapping
    @Cacheable
    @Operation(
        summary = "Listar Categorias",
        description = "Retorna um array com todas as categorias cadastradas."
    )
    public List<Categoria> index() {
        return repository.findAll();

    }

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Cadastrar Categoria",
        description = "Cadastra uma nova categoria para o usuário logado com os dados enviados no corpo da requisição."
    )
    @ApiResponses({ 
        @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
    public Categoria create(@RequestBody @Valid Categoria categoria) { // binding
        log.info("cadastrando categoria {} ", categoria);
        return repository.save(categoria);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Categoria> show(@PathVariable Long id) {
        log.info("buscando categoria por id {}", id);
        
        return repository
        .findById(id)
        .map(ResponseEntity::ok) // reference method
        .orElse(ResponseEntity.notFound().build());
        
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void destroy(@PathVariable Long id) {
        log.info("apagando categoria");
        
        verificarSeExisteCategoria(id);
        repository.deleteById(id);
    }
    
    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    public Categoria update(@PathVariable Long id, @RequestBody Categoria categoria) {
        log.info("atualizando categoria com id {} para {}", id, categoria);

        verificarSeExisteCategoria(id);
        categoria.setId(id);
        return repository.save(categoria);
    }

    private void verificarSeExisteCategoria(Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não existe categoria com o id informado. Consulte lista em /categoria"));
    }

}
