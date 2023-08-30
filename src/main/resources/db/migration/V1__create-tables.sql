CREATE TABLE usuarios (
	id int not null auto_increment primary key,
    nome varchar(70) not null,
    cpf bigint not null,
    telefones varchar(255) not null,
    logradouro varchar(255) not null,
    numero int not null,
    bairro varchar(30) not null,
    cidade varchar(30) not null,
    uf char(2) not null,
    cep int not null,
    complemento varchar(255) not null,
    login varchar(30) not null unique,
    password varchar(30) null,
    role varchar(30) not null default 'USER',
    ativo tinyint not null default true
);

CREATE TABLE livros (
	id int not null auto_increment primary key,
    titulo varchar(100) not null,
    categoria varchar(30) not null default 'nao identificada',
    preco float(8,2) not null default 0.00,
    descricao text null,
    ativo tinyint not null default true
);

CREATE TABLE pedidos (
	id int not null auto_increment primary key,
    valor_total float(9,2) not null default 0.00,
    data datetime not null default current_timestamp,
    id_usuario int not null,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE livros_usuarios (
	id int not null auto_increment primary key,
    comentario text null,
    avaliacao int null,
    data datetime not null default current_timestamp,
    id_livro int not null,
    id_usuario int not null,
    ativo tinyint not null default true,
    FOREIGN KEY (id_livro) REFERENCES livros(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE livros_pedidos (
	id int not null auto_increment primary key,
    preco_unitario float(8,2) not null default 0.00,
    quantidade int not null default 0,
    id_livro int not null,
    id_pedido int not null,
    FOREIGN KEY (id_livro) REFERENCES livros(id),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id)
);