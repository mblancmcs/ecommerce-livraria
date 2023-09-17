package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.DadosAtualizarLivroUsuario;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.DadosListagemLivroUsuario;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.LivroUsuarioRepository;
import biz.blancoder.ecommercelivraria.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroUsuarioRepository livroUsuarioRepository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = "nome")Pageable paginacao) {
        var paginaUsuario = usuarioRepository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(paginaUsuario);
    }

    @GetMapping("/cpf={cpf}")
    public ResponseEntity<DadosListagemUsuario> listarPorCpf(@PathVariable Long cpf) {
        if(!usuarioRepository.existsByCpfAndAtivoTrue(cpf)) {
            throw new ValidacaoException("Usuario inexistente ou inativo");
        }
        var usuario = usuarioRepository.findByCpfAndAtivoTrue(cpf);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhesUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        if(usuarioRepository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Ja existe um usuario com esse login");
        }
        var usuario = new Usuario(dados);
        usuarioRepository.save(usuario);
        var uri = uriBuilder.path("/usuario/id={id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhesUsuario(usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> atualizar(@RequestBody @Valid DadosAtualizarUsuario dados) {
        if(!usuarioRepository.existsByIdAndAtivoTrue(dados.id())) {
            throw new ValidacaoException("Id do usuario inexistente ou já inativo");
        }
        var usuario = usuarioRepository.getReferenceById(dados.id());
        usuario.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/id={id}")
    @Transactional
    public ResponseEntity inativarUsuario(@PathVariable Integer id) {
        if(!usuarioRepository.existsByIdAndAtivoTrue(id)) {
            throw new ValidacaoException("Id do usuario inexistente ou já inativo");
        }
        var usuario = usuarioRepository.getReferenceById(id);
        usuario.exclusaoLogica();
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/livro_usuario")
//    public ResponseEntity listarLivros() {
//        List<DadosListagemLivroUsuario> listaLivros = livroUsuarioRepository.findAllByUsuarioId();
//    }

    @PutMapping("/livro_usuario/avaliacao")
    @Transactional
    public ResponseEntity avaliarLivro(@RequestBody @Valid DadosAtualizarLivroUsuario dados) {
        if(!livroUsuarioRepository.existsByIdAndAtivoTrue(dados.id())) {
            throw new ValidacaoException("O usuário não pode avaliar livros que não possui ou inativos");
        }
        var livroUsuario = livroUsuarioRepository.getReferenceById(dados.id());
        livroUsuario.atualizarInformacoes(dados);
        return ResponseEntity.ok().body(new DadosListagemLivroUsuario(livroUsuario));
    }

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
