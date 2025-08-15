CREATE TABLE IF NOT EXISTS clientes_integracao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_cliente_integracao VARCHAR(50) NULL,
    codigo_cliente_omie VARCHAR(50) ,
    email VARCHAR(255) NOT NULL UNIQUE,
    razao_social VARCHAR(255) ,
    nome_fantasia VARCHAR(255) ,
    cidade VARCHAR(100),
    estado VARCHAR(100),
    id_user VARCHAR(255),
    status ENUM('Ativo', 'Bloqueado') NOT NULL DEFAULT 'Bloqueado', -- <--- AQUI!
    tipo_de_servico ENUM('Varejo', 'FoodService') NOT NULL DEFAULT 'Varejo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ALTER TABLE clientes_integracao 
-- MODIFY COLUMN status ENUM('Aprovado', 'Bloqueado') NOT NULL;