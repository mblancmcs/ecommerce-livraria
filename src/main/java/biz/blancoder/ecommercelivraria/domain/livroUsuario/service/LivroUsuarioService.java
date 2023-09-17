package biz.blancoder.ecommercelivraria.domain.livroUsuario.service;

import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.LivroUsuario;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.LivroUsuarioRepository;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroUsuarioService {

    @Autowired
    private LivroUsuarioRepository livroUsuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    public void cadastrarLivroUsuario(Pedido pedido, Usuario usuario) {
        if(pedido.getStatus().equals("pago")) {
            for(LivroPedido livroPedido : pedido.getLivroPedido()) {
                if(!livroUsuarioRepository.existsByUsuarioIdAndLivroId(usuario.getId(), livroPedido.getLivro().getId())) {
                    var livro = livroRepository.getReferenceById(livroPedido.getLivro().getId());
                    livroUsuarioRepository.save(new LivroUsuario(usuario, livro));
                }
            }
        }
    }

}
