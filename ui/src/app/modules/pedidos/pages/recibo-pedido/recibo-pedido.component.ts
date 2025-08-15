import { Component, AfterViewInit } from '@angular/core';
import { HeaderSignalService } from 'src/app/header-signal.service';

@Component({
  selector: 'app-recibo-pedido',
  templateUrl: './recibo-pedido.component.html',
  styleUrls: ['./recibo-pedido.component.css']
})
export class ReciboPedidoComponent implements AfterViewInit {
  pedido = {
    codigo: '123456789',
    data: new Date(),
    cupom: '',
    valorProdutos: 179.80,
    valorFrete: 15.00,
    total: 194.80,
    itens: [
      { nome: 'Costela Prime Rib BBQ', quantidade: 2, preco: 89.90, total: 179.80 },
      { nome: 'Costela Prime com Chimichurri', quantidade: 1, preco: 92.50, total: 92.50 }
    ]
  };

  constructor(
    private headerSignal: HeaderSignalService,
  ) {}

  baixarPDF() {
    // Implementar geração de PDF
    alert('Função de baixar PDF em desenvolvimento!');
  }

 ngAfterViewInit(): void {
    if (this.headerSignal) {
      this.headerSignal.title.set('Recibo do Pedido');
      this.headerSignal.actionPage.set(null);
      this.headerSignal.breadcrumbs.set([
        { label: 'Início', url: '/' },
        { label: 'Recibos', url: '/pedidos' }
      ]);
      this.headerSignal.actionButton.set(null);
    }
  }

}
