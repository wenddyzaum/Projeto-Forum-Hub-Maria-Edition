package br.com.wenddyzaum.forumhub.repository;

import br.com.wenddyzaum.forumhub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensagem ( String titulo, String mensagem);

    List<Topico> findByCurso(String curso);

}
