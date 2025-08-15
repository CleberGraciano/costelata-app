export interface Produto {
  id?: number;
  codigo: string;
  descricao: string;
  descricaoDetalhada?: string;
  unidade: string;
  valorUnitario: number;
  codigoCategoria: string;
  nomeCategoria?: string;
  status?: string;
  codigo_produto_integracao?: string;
  codigoProdutoIntegracao?: string;
  codigo_produto?: string;
}
