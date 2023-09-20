package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.DadosAtualizarLivroUsuario;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.DadosListagemLivroUsuario;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.LivroUsuarioRepository;
import biz.blancoder.ecommercelivraria.domain.usuario.*;
import biz.blancoder.ecommercelivraria.domain.usuario.validadoresClientes.ValidarCliente;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroUsuarioRepository livroUsuarioRepository;

    @Autowired
    private List<ValidarCliente> validadoresClientes;

    @Secured("ROLE_FUNCIONARIO")
    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = "nome")Pageable paginacao) {
        var paginaUsuario = usuarioRepository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(paginaUsuario);
    }

    @Secured("ROLE_FUNCIONARIO")
    @GetMapping("/cpf={cpf}")
    public ResponseEntity<DadosListagemUsuario> listarPorCpf(@PathVariable Long cpf) {
        if(!usuarioRepository.existsByCpfAndAtivoTrue(cpf)) {
            throw new ValidacaoException("Usuario inexistente ou inativo");
        }
        var usuario = usuarioRepository.findByCpfAndAtivoTrue(cpf);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @GetMapping("/id={id}")
    public ResponseEntity<DadosListagemUsuario> listarPorId(@PathVariable Integer id) {
        validadoresClientes.forEach(validador -> validador.validarCliente(id));
        var usuario = usuarioRepository.findByIdAndAtivoTrue(id);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @PutMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> atualizar(@RequestBody @Valid DadosAtualizarUsuario dados) {
        validadoresClientes.forEach(validador -> validador.validarCliente(dados.id()));
        var usuario = usuarioRepository.getReferenceById(dados.id());
        usuario.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @DeleteMapping("/id={id}")
    @Transactional
    public ResponseEntity inativarUsuario(@PathVariable Integer id) {
        validadoresClientes.forEach(validador -> validador.validarCliente(id));
        var usuario = usuarioRepository.getReferenceById(id);
        usuario.exclusaoLogica();
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_FUNCIONARIO")
    @GetMapping("/livro_usuario")
    public ResponseEntity<Page<DadosListagemLivroUsuario>> listarLivrosUsuarios(@PageableDefault(size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable paginacao) {
        var paginaLivrosUsuarios = livroUsuarioRepository.findAllByAtivoTrue(paginacao).map(DadosListagemLivroUsuario::new);
        return ResponseEntity.ok(paginaLivrosUsuarios);
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @GetMapping("/livro_usuario/id={idUsuario}")
    public ResponseEntity<Page<DadosListagemLivroUsuario>> listarLivros(@PageableDefault(size = 10, sort = "livro.titulo") Pageable paginacao,
                                                                        @PathVariable Integer idUsuario) {
        validadoresClientes.forEach(validador -> validador.validarCliente(idUsuario));
        var paginaLivrosUsuario = livroUsuarioRepository.findAllByUsuarioId(idUsuario, paginacao).map(DadosListagemLivroUsuario::new);
        return ResponseEntity.ok(paginaLivrosUsuario);
    }

    @Secured("ROLE_CLIENTE")
    @PutMapping("/livro_usuario/avaliacao")
    @Transactional
    public ResponseEntity avaliarLivro(@RequestBody @Valid DadosAtualizarLivroUsuario dados) {
        if(!livroUsuarioRepository.existsByIdAndAtivoTrue(dados.id())) {
            throw new ValidacaoException("O usuário não pode avaliar livros que não possui ou inativos");
        }
        var livroUsuario = livroUsuarioRepository.getReferenceById(dados.id());
        var usuarioAutenticado = usuarioRepository.getUsuarioByLogin(SecurityContextHolder.getContext().getAuthentication().getName());

        validadoresClientes.forEach(validador -> validador.validarCliente(usuarioAutenticado.getId()));
        if(livroUsuario.getUsuario().getId() != usuarioAutenticado.getId()) {
            throw new ValidacaoException("O usuário não pode avaliar livros de outros clientes");
        }

        livroUsuario.atualizarInformacoes(dados);
        return ResponseEntity.ok().body(new DadosListagemLivroUsuario(livroUsuario));
    }

    @Secured("ROLE_FUNCIONARIO")
    @DeleteMapping("/livro_usuario/id={id}")
    @Transactional
    public ResponseEntity inativarLivroUsuario(@PathVariable Integer id) {
        if(!livroUsuarioRepository.existsByIdAndAtivoTrue(id)) {
            throw new ValidacaoException("Id inexistente ou já inativo");
        }
        var livroUsuario = livroUsuarioRepository.getReferenceById(id);
        livroUsuario.exclusaoLogica();
        return ResponseEntity.noContent().build();
    }

}
