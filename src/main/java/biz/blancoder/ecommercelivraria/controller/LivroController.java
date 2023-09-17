package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livro.*;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/livro")
public class LivroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroLivro dados, UriComponentsBuilder uriBuilder) {
        var livro = new Livro(dados);
        livroRepository.save(livro);
        var uri = uriBuilder.path("/livro/id={id}").buildAndExpand(livro.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemLivro(livro));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemLivro>> listar(@PageableDefault(size = 10, sort = "titulo") Pageable paginacao) {
        var paginaLivros = livroRepository.findAllByAtivoTrue(paginacao).map(DadosListagemLivro::new);
        return ResponseEntity.ok(paginaLivros);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizarLivro dados) {
        if(!livroRepository.existsByIdAndAtivoTrue(dados.id())) {
            throw new ValidacaoException("Id informado inexistente ou inativo");
        }
        var livro = livroRepository.getReferenceById(dados.id());
        livro.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemLivro(livro));
    }

    @DeleteMapping("/id={id}")
    @Transactional
    public ResponseEntity exclusaoLogica(@PathVariable Integer id) {
        if(!livroRepository.existsByIdAndAtivoTrue(id)) {
            throw new ValidacaoException("Id informado inexistente ou inativo");
        }
        var livro = livroRepository.getReferenceById(id);
        livro.exclusaoLogica();
        return ResponseEntity.noContent().build();
    }

}
