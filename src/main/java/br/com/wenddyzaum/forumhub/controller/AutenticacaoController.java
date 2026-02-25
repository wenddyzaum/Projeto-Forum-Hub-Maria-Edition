package br.com.wenddyzaum.forumhub.controller;

import br.com.wenddyzaum.forumhub.dto.DadosAutenticacao;
import br.com.wenddyzaum.forumhub.dto.DadosTokenJWT;
import br.com.wenddyzaum.forumhub.model.Usuario;
import br.com.wenddyzaum.forumhub.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DadosAutenticacao dados) {
        // Cria o token de autenticação interno do Spring com login e senha
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        // Autentica o usuário (chama o AutenticacaoService e confere a senha)
        var authentication = manager.authenticate(authenticationToken);

        // Gera o Token JWT real
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // Retorna o JSON { "token": "valor_do_token" }
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}