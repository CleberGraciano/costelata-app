package com.integracaoOmie.integracaoOmie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integracaoOmie.integracaoOmie.model.Pedido.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
