CREATE TABLE transportadoras (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    inscricao_estadual VARCHAR(50),
    endereco VARCHAR(255),
    telefone VARCHAR(30),
    status ENUM('Ativo', 'Bloqueado') NOT NULL DEFAULT 'Ativo'
);
