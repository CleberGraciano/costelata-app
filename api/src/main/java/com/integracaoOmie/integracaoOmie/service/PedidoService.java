package com.integracaoOmie.integracaoOmie.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.dto.Pedido.PedidoDTO;
import com.integracaoOmie.integracaoOmie.dto.Pedido.PedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Pedido.Pedido;
import com.integracaoOmie.integracaoOmie.model.Pedido.ItemPedido;
import com.integracaoOmie.integracaoOmie.model.Product.Product;
import com.integracaoOmie.integracaoOmie.repository.PedidoRepository;
import com.integracaoOmie.integracaoOmie.repository.ProductRepository;
import com.integracaoOmie.integracaoOmie.utils.PedidoMapper;

@Service

public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PedidoMapper mapper;

    public List<PedidoResponseDTO> listar() {
        List<Pedido> listagem = pedidoRepo.findAll();
        return mapper.toResponseDTOList(listagem);

    }

    public PedidoResponseDTO buscar(Long id) {
        Pedido pedidoEncontrado = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return mapper.toResponseDTO(pedidoEncontrado);
    }

    public PedidoResponseDTO criar(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setCliente(dto.getCliente());
        pedido.setDataEmissao(LocalDate.parse(dto.getDataEmissao()));
        pedido.setStatus(dto.getStatus());
        pedido.setValorTotal(dto.getValorTotal());

        List<ItemPedido> itens = dto.getItens().stream().map(i -> {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            Product produto = productRepo.findById(i.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            item.setProduto(produto);
            item.setQuantidade(i.getQuantidade());
            item.setValorUnitario(i.getValorUnitario());
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itens);
        pedidoRepo.save(pedido);
        return mapper.toResponseDTO(pedido);

    }

    public void deletar(Long id) {
        if (!pedidoRepo.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado");
        }
        pedidoRepo.deleteById(id);
    }

    public PedidoResponseDTO atualizar(Long id, PedidoDTO dto) {
        Pedido pedidoExistente = pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoExistente.setNumeroPedido(dto.getNumeroPedido());
        pedidoExistente.setCliente(dto.getCliente());
        pedidoExistente.setDataEmissao(LocalDate.parse(dto.getDataEmissao()));
        pedidoExistente.setStatus(dto.getStatus());
        pedidoExistente.setValorTotal(dto.getValorTotal());

        pedidoExistente.getItens().clear();

        List<ItemPedido> novosItens = dto.getItens().stream().map(itemDTO -> {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedidoExistente);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setValorUnitario(itemDTO.getValorUnitario());

            Product produto = productRepo.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            item.setProduto(produto);

            return item;
        }).collect(Collectors.toList());

        pedidoExistente.setItens(novosItens);

        Pedido pedidoAtualizado = pedidoRepo.save(pedidoExistente);

        return mapper.toResponseDTO(pedidoAtualizado);
    }

}
