import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor() { }

  listarStatus() {
    return [
      { codigo: 'Ativo', descricao: 'Ativo' },
      { codigo: 'Bloqueado', descricao: 'Bloqueado' }
    ];
  }

  listarStatusPedidos() {
    return [
      {
        "cDescricao": "Ativo",
        "cObservacao": "",
        "nCodigo": 5371142798
      },
      {
        "cDescricao": "Cancelado",
        "cObservacao": "",
        "nCodigo": 5371142799
      },
      {
        "cDescricao": "Perdido",
        "cObservacao": "",
        "nCodigo": 5371142800
      },
      {
        "cDescricao": "Suspenso",
        "cObservacao": "",
        "nCodigo": 5371142801
      },
      {
        "cDescricao": "Conquistado",
        "cObservacao": "",
        "nCodigo": 5371142802
      }
    ];
  }
}
