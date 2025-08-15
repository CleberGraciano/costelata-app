import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EstadosService {

  constructor() { }

  listarEstados() {
    return [
      { "codigo": "AC", "descricao": "Acre" },
      { "codigo": "AL", "descricao": "Alagoas" },
      { "codigo": "AP", "descricao": "Amapá" },
      { "codigo": "AM", "descricao": "Amazonas" },
      { "codigo": "BA", "descricao": "Bahia" },
      { "codigo": "CE", "descricao": "Ceará" },
      { "codigo": "DF", "descricao": "Distrito Federal" },
      { "codigo": "ES", "descricao": "Espírito Santo" },
      { "codigo": "GO", "descricao": "Goiás" },
      { "codigo": "MA", "descricao": "Maranhão" },
      { "codigo": "MT", "descricao": "Mato Grosso" },
      { "codigo": "MS", "descricao": "Mato Grosso do Sul" },
      { "codigo": "MG", "descricao": "Minas Gerais" },
      { "codigo": "PA", "descricao": "Pará" },
      { "codigo": "PB", "descricao": "Paraíba" },
      { "codigo": "PR", "descricao": "Paraná" },
      { "codigo": "PE", "descricao": "Pernambuco" },
      { "codigo": "PI", "descricao": "Piauí" },
      { "codigo": "RJ", "descricao": "Rio de Janeiro" },
      { "codigo": "RN", "descricao": "Rio Grande do Norte" },
      { "codigo": "RS", "descricao": "Rio Grande do Sul" },
      { "codigo": "RO", "descricao": "Rondônia" },
      { "codigo": "RR", "descricao": "Roraima" },
      { "codigo": "SC", "descricao": "Santa Catarina" },
      { "codigo": "SP", "descricao": "São Paulo" },
      { "codigo": "SE", "descricao": "Sergipe" },
      { "codigo": "TO", "descricao": "Tocantins" }
    ];
  }
}
