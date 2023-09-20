package biz.blancoder.ecommercelivraria.domain.usuario.validadoresClientes;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidarExistenciaAtivo implements ValidarCliente {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validarCliente(Integer id) {
        var usuarioAutenticado = usuarioRepository.getUsuarioByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!usuarioRepository.existsByIdAndAtivoTrue(id)) {
            throw new ValidacaoException("Id do usuario inexistente ou inativo");
        }

    }
}
