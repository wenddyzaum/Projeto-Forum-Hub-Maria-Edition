package br.com.wenddyzaum.forumhub.controller;

import br.com.wenddyzaum.forumhub.dto.DadosCadastroTopico;
import br.com.wenddyzaum.forumhub.model.Topico;
import br.com.wenddyzaum.forumhub.repository.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {

        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest()
                    .body("Tópico já cadastrado com mesmo título e mensagem.");
        }

        Topico topico = repository.save(
                new Topico(
                        dados.titulo(),
                        dados.mensagem(),
                        dados.autor(),
                        dados.curso()
                )
        );

        return ResponseEntity
                .created(URI.create("/topicos/" + topico.getId()))
                .body(topico);
    }


    @GetMapping
    public ResponseEntity<List<Topico>> listar(@RequestParam(required = false) String curso) {

        if (curso != null) {
            return ResponseEntity.ok(repository.findByCurso(curso));
        }

        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topico> detalhar(@PathVariable Long id) {

        var topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {
            return ResponseEntity.ok(topicoOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topico> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroTopico dados) {

        var topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {

            Topico topico = topicoOptional.get();

            topico.setTitulo(dados.titulo());
            topico.setMensagem(dados.mensagem());
            topico.setAutor(dados.autor());
            topico.setCurso(dados.curso());

            repository.save(topico);

            return ResponseEntity.ok(topico);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        var topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}