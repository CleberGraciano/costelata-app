import { Injectable } from '@angular/core';
import { EventEmitter } from '@angular/core';

export interface Produto {
  id: number;
  nome: string;
  preco: number;
  imagem: string;
  descricao?: string;
  qtdAdicionar?: number;
  imgHover?: boolean;
  estoque: number;
  freteGratis?: boolean;
  maisVendido?: boolean;
  novo?: boolean;
  categoria?: string; // Adicionando categoria para filtragem
}

export interface ItemCarrinho {
  produto: Produto;
  quantidade: number;
}

@Injectable({ providedIn: 'root' })
export class CarrinhoService {
  private itens: ItemCarrinho[] = [];

  carrinhoAberto = new EventEmitter<void>();

  getCarrinho(): ItemCarrinho[] {
    return [...this.itens];
  }

  adicionar(produto: Produto, quantidade: number = 1) {
    if (quantidade < 1) return;
    const item = this.itens.find(i => i.produto.id === produto.id);
    if (item) {
      item.quantidade += quantidade;
    } else {
      this.itens.push({ produto, quantidade });
    }
    this.carrinhoAberto.emit();
  }

  remover(produto: Produto) {
    const idx = this.itens.findIndex(i => i.produto.id === produto.id);
    if (idx > -1) {
      if (this.itens[idx].quantidade > 1) {
        this.itens[idx].quantidade--;
      } else {
        this.itens.splice(idx, 1);
      }
    }
  }

  limpar() {
    this.itens = [];
  }

  getTotal(): number {
    return this.itens.reduce((sum, item) => sum + item.produto.preco * item.quantidade, 0);
  }
}
