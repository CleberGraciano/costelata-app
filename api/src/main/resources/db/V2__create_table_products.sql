
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    descricao_detalhada TEXT,
    unidade VARCHAR(20),
    valor_unitario DECIMAL(10,2) NOT NULL,
    codigo_categoria VARCHAR(100),
    nome_categoria VARCHAR(255),
        degelo VARCHAR(255),
        armazenamento VARCHAR(255),
        rendimento_por_produto VARCHAR(255),
        argumentos_venda VARCHAR(255),
        exposicao_pdv VARCHAR(255),
        explicacoes_etiquetas VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
