export interface Pedido {
  id?: number;
  numeroPedido: string;
  dataEmissao: string;
  cliente: string;
  valorTotal: number;
  status: string;
  itens: Array<{
    produto: string;
    quantidade: number;
    valorUnitario: number;
  }>;
}
