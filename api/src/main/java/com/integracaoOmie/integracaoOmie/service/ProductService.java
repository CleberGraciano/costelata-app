package com.integracaoOmie.integracaoOmie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.dto.Product.ProductDTO;
import com.integracaoOmie.integracaoOmie.model.Product.Product;
import com.integracaoOmie.integracaoOmie.repository.ProductRepository;
import com.integracaoOmie.integracaoOmie.specification.ProductSpecification;
import com.integracaoOmie.integracaoOmie.specification.SpecificationBuilder;
import com.integracaoOmie.integracaoOmie.utils.filters.ProductFilter;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> listar() {
        return repo.findAll();
    }

    public Product buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Product criar(Product produto) {
        Product salvo = repo.save(produto);
        return salvo;
    }

    public Product atualizar(Long id, ProductDTO productDTO) {
        Product existente = buscar(id);
        existente.setCodigo(productDTO.getCodigo());
        existente.setCodigoCategoria(productDTO.getCodigoCategoria());
        existente.setDescricao(productDTO.getDescricao());
        existente.setDescricaoDetalhada(productDTO.getDescricaoDetalhada());
        existente.setNomeCategoria(productDTO.getNomeCategoria());
        existente.setUnidade(productDTO.getUnidade());
        existente.setValorUnitario(productDTO.getValorUnitario());

        return repo.save(existente);
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        repo.deleteById(id);
    }

    public Page<ProductDTO> listarPaginado(ProductFilter filter, Pageable pageable) {
        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();

        if (filter.getDescricao() != null)
            builder.with("descricao", ":", filter.getDescricao());

        if (filter.getValorMin() != null)
            builder.with("valorUnitario", ">", filter.getValorMin());

        if (filter.getValorMax() != null)
            builder.with("valorUnitario", "<", filter.getValorMax());

        Specification<Product> spec = builder.build(ProductSpecification::new);

        return repo.findAll(spec, pageable)
                .map(this::toDTO);
    }

    private ProductDTO toDTO(Product produto) {
        ProductDTO dto = new ProductDTO();
        dto.setCodigo(produto.getCodigo());
        dto.setDescricao(produto.getDescricao());
        dto.setDescricaoDetalhada(produto.getDescricaoDetalhada());
        dto.setUnidade(produto.getUnidade());
        dto.setValorUnitario(produto.getValorUnitario());
        dto.setCodigoCategoria(produto.getCodigoCategoria());
        dto.setNomeCategoria(produto.getNomeCategoria());
        return dto;
    }

}
