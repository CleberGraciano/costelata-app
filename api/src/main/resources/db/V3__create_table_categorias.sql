
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    status ENUM('Ativo', 'Bloqueado') NOT NULL DEFAULT 'Ativo'
);
