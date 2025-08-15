export interface Usuario {
  id?: any;
  codigo_cliente_omie?: string;
  codigo_cliente_integracao?: string;
  nome: string;
  nomeFantasia: string;
  razaoSocial: string;
  cnpjCpf: string;
  telefone?: string;
  email: string;
  endereco?: string;
  status: string;
  role: any;
  senhaProvisoria?: string;
}
