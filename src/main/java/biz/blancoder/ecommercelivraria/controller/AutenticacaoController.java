package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.domain.usuario.*;
import biz.blancoder.ecommercelivraria.infra.security.DadosTokenJWT;
import biz.blancoder.ecommercelivraria.infra.security.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoUsuario dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        if(usuarioRepository.findByLogin(dados.login()) != null) return ResponseEntity.badRequest().build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(dados.password());
        Usuario novoUsuario = new Usuario(dados);
        novoUsuario.alterarSenha(senhaCriptografada);
        novoUsuario.alterarPerfil(PerfilUsuario.CLIENTE);
        usuarioRepository.save(novoUsuario);

        var uri = uriBuilder.path("/usuario/id={id}").buildAndExpand(novoUsuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhesUsuario(novoUsuario));
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/admin/registrar")
    @Transactional
    public ResponseEntity adminRegistrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        if(usuarioRepository.findByLogin(dados.login()) != null) return ResponseEntity.badRequest().build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(dados.password());
        Usuario novoUsuario = new Usuario(dados);
        novoUsuario.alterarSenha(senhaCriptografada);
        if(dados.perfil() != null) novoUsuario.alterarPerfil(dados.perfil());
        else novoUsuario.alterarPerfil(PerfilUsuario.FUNCIONARIO);
        usuarioRepository.save(novoUsuario);

        var uri = uriBuilder.path("/usuario/id={id}").buildAndExpand(novoUsuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(novoUsuario));
    }

}
