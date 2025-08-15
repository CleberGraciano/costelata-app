package com.integracaoOmie.integracaoOmie.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.integracaoOmie.integracaoOmie.dto.Pedido.ItemPedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Pedido.PedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Pedido.Pedido;

@Component

public class PedidoMapper {
    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setDataEmissao(pedido.getDataEmissao());
        dto.setCliente(pedido.getCliente());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setStatus(pedido.getStatus());

        List<ItemPedidoResponseDTO> itens = pedido.getItens().stream().map(item -> {
            ItemPedidoResponseDTO itemDTO = new ItemPedidoResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProdutoId(item.getProduto().getId());
            itemDTO.setProdutoDescricao(item.getProduto().getDescricao());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setValorUnitario(item.getValorUnitario());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItens(itens);
        return dto;
    }

    public List<PedidoResponseDTO> toResponseDTOList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

}
