// Model genérico para resposta paginada do backend
export interface PaginacaoResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

export interface ClienteTag {
  tag: string;
}

export interface ClienteDadosBancarios {
  agencia: string;
  cChavePix: string;
  codigo_banco: string;
  conta_corrente: string;
  doc_titular: string;
  nome_titular: string;
  transf_padrao: string;
}

export interface ClienteInfo {
  dAlt: string;
  dInc: string;
  hAlt: string;
  hInc: string;
  uAlt: string;
  uInc: string;
  cImpAPI: string;
}

export interface ClienteRecomendacoes {
  gerar_boletos: string;
  tipo_assinante: string;
  codigo_vendedor: string;
}

export interface ClienteEnderecoEntrega {
  rawData: {
    [key: string]: any;
  };
}

export interface Cliente {
  id?: any;
  idUser?: any;
  tags: ClienteTag[];
  status: string;
  codigo_cliente_omie: number;
  codigo_cliente_integracao: string;
  razao_social: string;
  nome_fantasia: string;
  nomeFantasia?: string;
  cnpj_cpf: string;
  inscricao_estadual: string;
  inscricao_municipal: string;
  pessoa_fisica: string;
  endereco: string;
  email: string;
  endereco_numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  cidade_ibge: string;
  estado: string;
  cep: string;
  codigo_pais: string;
  exterior: string;
  bloquear_faturamento: string;
  enviar_anexos: string;
  inativo: string;
  dados_bancarios: ClienteDadosBancarios;
  info: ClienteInfo;
  recomendacoes: ClienteRecomendacoes;
  endereco_entrega: ClienteEnderecoEntrega;
  senhaProvisoria?: string;
  condicaoDePagamento?: string;
}

export interface ClienteResponse {
  id?: number;
  codigo_cliente_integracao?: string;
  codigoClienteIntegracao?: string | undefined;
  codigo_cliente_omie?: string;
  razao_social?: string;
  email?: string;
  status?: 'Ativo' | 'Inativo' | string;
}
