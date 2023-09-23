# E-Library

API Rest de e-commerce para livrarias, com integração ao gateway de pagamentos da Pagar.me e seu webhook; desenvolvida em Java e com o framework Spring.

## Stack
- Java 17: versão Long-term Support (LTS) até aproximadamente setembro de 2029.
- Spring Boot: framework para aplicações Java de bastante utilização no mercado.
- Spring Data JPA: com a implementação do ORM Hibernate.
- Spring Security: para o controle de acesso de usuários com Json Web Tokens (JWT).
- Lombok: para geração de códigos repetitivos como de construtores, getters e setters.
- Bean Validation: para fazer validações de forma automática.
- Flyway: para gerenciar as migrações / alterações do banco de dados.
- MySQL: SGBD (Sistema Gerenciador de Banco de dados) usual com aplicações Java.
- Maven: gerenciador de dependências.
- SpringDoc: utiliza do padrão OpenAPI para documentar de forma automática o projeto, e com a melhor visualização pelo Swagger.
- PagarmeApiSDK: utilizado para realizar a tokenização do cartão de crédito.

## Requisitos
- Java (JRE ou JDK) - versão 17 (LTS).
- MySQL - a partir da versão 5.5.

## Execução

Para executar a versão de produção da aplicação, foi utilizado variáveis de ambiente para flexibilizar e deixar a aplicação mais segura, por esse motivo, para que não seja preciso configurá-las no servidor, é possível executar a aplicação através do seguinte comando (no diretório raíz da API):
```bash
java -Dspring.profiles.active=prod -DDATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce_livraria_prod?createDatabaseIfNotExist=true -DDATASOURCE_USERNAME=root -DDATASOURCE_PASSWORD=root -DP_API_KEY_PAGARME=<chave_publica_pagarme> -DS_API_KEY_PAGARME=<chave_privada_pagarme:_base64> -jar ./target/ecommerce-livraria-0.0.1-SNAPSHOT.jar
```
É necessário observar que:
- O "-Dspring.profiles.active=prod" é para executar o projeto em sua versão de produção;
- É necessário alterar o valor das variáveis de ambiente "DATASOURCE_USERNAME" e "DATASOURCE_PASSWORD" para respectivamente, o nome e senha configurados do banco de dados MySQL, a não ser que também tenham o valor "root";
- Na variável de ambiente "DATASOURCE_URL", conferir se a porta do banco de dados MySQL é a padrão 3306 ou se será preciso alterar no comando acima para a configurada no servidor;
- É preciso alterar os valores das chaves públicas e privadas de acordo com o que a Pagar.me oferece a seus usuários, através das variáveis de ambiente "P_API_KEY_PAGARME" e "S_API_KEY_PAGARME", porém na última é necessário adicionar o caracter ":" de dois pontos após o valor da chave, e criptografar em "base64";
- É possível testar o webhook em localhost usando o ngrock, instalando-o e executando o comando “ngrok http 8080” para Linux ou “ngrok.exe http 8080” para Windows; onde ao gerar a url temporária, é preciso indicá-la nas configurações de webhook do Pagar.me concatenado com o endpoint do webhook. Exemplo: “https://ff8f-28….ngrok.io/pedido/webhook_atualizar”;
- Para simular os meios de pagamento de pix e boleto, é preciso alterar nas configurações de conta do Pagar.me de “PSP” para “Simulator”;
- O Tomcat utiliza a porta padrão 8080 para execução da API.
- Ao fazer login, é necessário informar o "Bearer Token" / JWT recebido na resposta, no cabeçalho "Authorization" das próximas requisições;
- Ao executar a aplicação, é possível fazer os testes e entender melhor como a API funciona através do Swagger, pelo endereço: http://localhost:8080/swagger-ui/index.html

## E-Library

### Autenticação e autorização
Usuário: admin

Senha: 123456

- Ao acessar com o usuário administrador, é possível cadastrar novos com os acessos de: "CLIENTE", "FUNCIONARIO" ou "ADMIN", pelo seu respectivo endpoint: auth/admin/registrar.
- O cliente terá algumas autorizações em comum com o funcionário e administrador, além de endpoints exclusivos.
- O funcionário terá as autorizações que forem necessárias, sendo algumas em comum com o cliente e administrador.
- O administrador terá seus endpoints exclusivos, assim como todos pertencentes ao funcionário.

Obs.: Além do usuário administrador, também foi criado um cliente (user_cliente) e funcionário (user_funcionario) com a mesma senha do usuário admin: 123456, para que se for preciso, facilite / agilize os testes da aplicação.

![autenticacao](https://github.com/mblancmcs/ecommerce-livraria/assets/77879631/00d1bee5-599a-4fa8-a44f-581278281bef)

[POST] /auth/login: acesso dos usuários.

[POST] /auth/registrar: registro de todos os clientes.

[POST] /auth/admin/registrar: exclusivo do administrador para registrar usuários de qualquer perfil.

### Modelo Entidade Relacionamento (MER) - Banco de dados
![MER](https://github.com/mblancmcs/ecommerce-livraria/assets/77879631/5ddb87d3-31e0-4710-a8aa-01ac11bfcb9a)

### Endpoints de usuários

![usuario](https://github.com/mblancmcs/ecommerce-livraria/assets/77879631/23f64124-109c-4c03-b175-cb94e4d06fc1)

Observações:
- Clientes logados não poderão fazer requisições para outros clientes, desde operações mais simples de "crud" ou avaliação de livros comprados, quanto de pagamento.
- Os livros do pedido só serão adicionados a tabela "livros_usuarios", quando o status do mesmo retornar (ou atualizar pelo webhook) para "pago", possibilitando que o cliente avalie com comentário e nota seus livros comprados.

Usuarios

[GET]    /usuario: listar usuários.

[GET]    /usuario/id={id}: listar usuário pelo id.

[GET]    /usuario/cpf={cpf}: listar usuário por CPF.

[PUT]    /usuario: atualizar informações de um usuário específico.

[DELETE] /usuario/id={id}: inativar usuário.

Livros de clientes

[GET]    /usuario/livro_usuario: listar os livros dos clientes.

[GET]    /usuario/livro_usuario/id={idUsuario}: listar os livros de um cliente específico.

[PUT]    /usuario/livro_usuario/avaliacao: clientes logados podem avaliar apenas os seus livros comprados.

[DELETE] /usuario/livro_usuario/id={id}: inativar livro de algum cliente.

### Endpoints de livros

![livro](https://github.com/mblancmcs/ecommerce-livraria/assets/77879631/f56dcb36-949d-4a80-8720-6a01c32ff1ea)

[GET]    /livro: listar livros.

[POST]   /livro: cadastrar livro.

[PUT]    /livro: atualizar informações de um livro específico.

[DELETE] /livro/id={id}: inativar um livro específico.

### Endpoints de pedidos

![pedido](https://github.com/mblancmcs/ecommerce-livraria/assets/77879631/bd898e5a-8448-417f-934c-32dc2ee61cfd)

[GET]    /pedido: listar pedidos.

[GET]    /pedido/id={id}: listar pedido pelo id.

[GET]    /pedido/id_usuario={id}: listar pedidos de um cliente específico.

[GET]    /pedido/status={status}: listar pedidos pelo status de pagamento.

[POST]   /pedido: cadastro de pedidos apenas pelo cliente.

[POST]   /pedido/webhook_atualizar: postback destinado a Pagar.me para atualizar as informações do pedido.

[POST]   /pedido/pagamento/cartao_credito: apenas clientes logados podem realizar o pagamento de seus pedidos através do cartão de crédito.

[POST]   /pedido/pagamento/pix: apenas clientes logados podem realizar o pagamento de seus pedidos por pix.

[POST]   /pedido/pagamento/boleto: apenas clientes logados podem realizar o pagamento de seus pedidos por boleto.

[DELETE] /pedido/id={id}: inativar pedido.

## Atualizações da aplicação

V.1 - Versão inicial.

## Contribuições

Pull requests são bem vindos. Para maiores alterações, por favor crie uma publicação para que seja discutido sobre.

Por favor faça o upload dos testes apropriados.

## Licença

[MIT](https://choosealicense.com/licenses/mit/)
