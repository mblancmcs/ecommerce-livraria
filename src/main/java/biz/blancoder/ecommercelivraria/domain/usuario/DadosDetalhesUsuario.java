package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.DadosEndereco;

import java.util.List;

public record DadosDetalhesUsuario(
        Integer id,
        String nome,
        Long cpf,
        List<String> telefones,
        DadosEndereco dadosEndereco,
        String login,
        String perfil
) {

    public DadosDetalhesUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(),usuario.getCpf(), usuario.getTelefones(), usuario.retornaEndereco(), usuario.getLogin(), usuario.getPerfil());
    }
}
