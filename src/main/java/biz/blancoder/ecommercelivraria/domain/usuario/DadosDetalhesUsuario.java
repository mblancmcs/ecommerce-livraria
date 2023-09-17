package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.DadosEndereco;

import java.util.List;

public record DadosDetalhesUsuario(
        Integer id,
        String nome,
        Long cpf,
        String email,
        List<String> telefones,
        DadosEndereco endereco,
        String login,
        PerfilUsuario perfil
) {

    public DadosDetalhesUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(),usuario.getCpf(), usuario.getEmail(), usuario.getTelefones(),
                usuario.retornaEndereco(), usuario.getLogin(), usuario.getPerfil());
    }
}
