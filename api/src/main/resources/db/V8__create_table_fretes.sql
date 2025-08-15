CREATE TABLE fretes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    transportadora_id BIGINT NOT NULL,
    valor_frete DECIMAL(10,2) NOT NULL,
    prazo_entrega_dias INT NOT NULL,
    pedido_minimo DECIMAL(10,2) NOT NULL,
    status ENUM('Ativo', 'Bloqueado') NOT NULL DEFAULT 'Ativo'
    CONSTRAINT fk_frete_transportadora FOREIGN KEY (transportadora_id) REFERENCES transportadoras(id)
);
