package biz.blancoder.ecommercelivraria.domain.endereco;

public record DadosEndereco(
        String logradouro,
        Integer numero,
        String bairro,
        String cidade,
        String uf,
        Integer cep,
        String complemento
) {

    public DadosEndereco(Endereco endereco) {
        this(endereco.getLogradouro(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade(), endereco.getUf(),
                endereco.getCep(), endereco.getComplemento());
    }

}
