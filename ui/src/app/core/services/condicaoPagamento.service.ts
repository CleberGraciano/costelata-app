import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class condicaoPagamentoService {

  constructor() { }

  listarCondicoes() {
    return [
      { codigo: 'AVISTA', descricao: 'À Vista' },
      { codigo: 'DIAS7', descricao: '7 dias' },
      { codigo: 'DIAS14', descricao: '14 dias' },
      { codigo: 'DIAS28', descricao: '28 dias' }
    ];
  }
}
