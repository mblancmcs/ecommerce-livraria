package biz.blancoder.ecommercelivraria.domain.usuario.validadoresClientes;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.usuario.PerfilUsuario;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidarMesmoCliente implements ValidarCliente {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validarCliente(Integer id) {
        var usuarioAutenticado = usuarioRepository.getUsuarioByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(usuarioAutenticado.getPerfil().equals(PerfilUsuario.CLIENTE) && usuarioAutenticado.getId() != id) {
            throw new ValidacaoException("Não é possível realizar ações para outros clientes");
        }

    }

}
