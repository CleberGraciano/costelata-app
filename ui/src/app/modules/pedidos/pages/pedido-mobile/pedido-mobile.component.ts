import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CarrinhoService } from '../../services/carrinho.service';
import type { Produto } from '../../services/carrinho.service';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';
import { Pedido } from '@modules/pedidos/models/pedidos.model';

@Component({
  selector: 'app-pedido-mobile',
  templateUrl: './pedido-mobile.component.html',
  styleUrls: ['./pedido-mobile.component.css']
})
export class PedidoMobileComponent extends BaseFormComponent<Pedido> implements OnInit, AfterViewInit {
  produtos: Produto[] = [
    {
      id: 1,
      nome: 'Costela Prime Rib BBQ',
      preco: 89.90,
      imagem: 'https://acdn-us.mitiendanube.com/stores/004/062/203/products/8-023908f4ec97ab7ab117373916999581-1024-1024.webp',
      descricao: 'Costela bovina assada lentamente, servida com molho barbecue artesanal.',
      estoque: 10,
      freteGratis: true,
      categoria: 'Prime Ribs',
      maisVendido: true,
      novo: false
    },
    {
      id: 2,
      nome: 'Costela Prime com Chimichurri',
      preco: 92.50,
      imagem: 'https://acdn-us.mitiendanube.com/stores/004/062/203/products/7-e0966adfa32d281a1817373916912659-1024-1024.webp',
      descricao: 'Costela premium temperada com chimichurri artesanal, pronta para servir.',
      estoque: 8,
      freteGratis: true,
      categoria: 'Prime Ribs',
      maisVendido: false,
      novo: true
    },
    {
      id: 3,
      nome: 'Costelata Enlatada Tradicional',
      preco: 29.90,
      imagem: 'https://acdn-us.mitiendanube.com/stores/004/062/203/products/7-e0966adfa32d281a1817373916912659-1024-1024.webp',
      descricao: 'Costela desfiada em lata com molho suave e temperos naturais.',
      estoque: 15,
      freteGratis: true,
      categoria: 'Enlatados',
      maisVendido: false,
      novo: false
    },
    {
      id: 4,
      nome: 'Costelata Enlatada Picante',
      preco: 31.90,
      imagem: 'https://acdn-us.mitiendanube.com/stores/004/062/203/products/7-e0966adfa32d281a1817373916912659-1024-1024.webp',
      descricao: 'Versão picante da nossa costela enlatada com molho barbecue apimentado.',
      estoque: 6,
      freteGratis: false,
      categoria: 'Enlatados',
      maisVendido: false,
      novo: true
    }
  ];

  categorias = [
    { nome: 'Prime Ribs', icone: 'assets/images/icones/categoria_1.png' },
    { nome: 'Enlatados', icone: 'assets/images/icones/categoria_2.png' },
    { nome: 'Merchandising', icone: 'assets/images/icones/categoria_3.png' },
    { nome: 'Congelados', icone: 'assets/images/icones/categoria_4.png' },
    { nome: 'Desfiados', icone: 'assets/images/icones/categoria_5.png' },
    { nome: 'Deli43', icone: 'assets/images/icones/categoria_6.png' }
  ];

  produtosFiltrados: Produto[] = [];
  categoriaSelecionada = 'Todos';
  mostrarFinalizar = false;
  descricao?: string;

  private carrinhoService = inject(CarrinhoService);

  override ngOnInit(): void {
    this.ordenarProdutos();

    this.form = this.fb.group({
      nome: ['', Validators.required],
      telefone: ['', Validators.required],
      endereco: ['', Validators.required],
      observacao: ['']
    });
  }

  ngAfterViewInit(): void {
    this.setHeader('Monte seu Pedido', 'pedidos');
  }

  protected override buildForm(): void {
    this.form = this.fb.group({}); // pode remover override se não for usado
  }

  protected override buscarDados(id: number): void {
    // implementar se for necessário buscar pedido por ID
  }

  setQtd(produto: Produto, valor: number) {
    produto.qtdAdicionar = Math.max(1, valor);
  }

  adicionarAoCarrinho(produto: Produto, qtd: number = 1) {
    if (qtd < 1) return;
    this.carrinhoService.adicionar(produto, qtd);
    // O evento carrinhoAberto será emitido pelo serviço
  }

  removerDoCarrinho(produto: Produto) {
    this.carrinhoService.remover(produto);
  }

  get carrinho() {
    return this.carrinhoService.getCarrinho();
  }

  get total() {
    return this.carrinhoService.getTotal();
  }

  get produtosExibidos() {
    return this.categoriaSelecionada === 'Todos'
      ? this.produtos
      : this.produtosFiltrados;
  }

  selecionarCategoria(categoria: string) {
    this.categoriaSelecionada = categoria;

    const categoriasDisponiveis = ['Todos', ...this.categorias.map(c => c.nome)];

    if (!categoriasDisponiveis.includes(categoria)) {
      this.produtosFiltrados = [];
      return;
    }

    this.produtosFiltrados = categoria === 'Todos'
      ? []
      : this.produtos.filter(p => p.categoria === categoria);
  }

  finalizarPedido() {
    if (this.form.valid && this.carrinho.length > 0) {
      alert('Pedido realizado com sucesso!');
      this.carrinhoService.limpar();
      this.form.reset();
      this.mostrarFinalizar = false;
    }
  }

  private ordenarProdutos() {
    this.produtos.sort((a, b) => {
      if (a.maisVendido) return -1;
      if (b.maisVendido) return 1;
      if (a.novo) return -1;
      if (b.novo) return 1;
      return 0;
    });
  }
}
