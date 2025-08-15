import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TipoServicoService {

  constructor() { }

  listarTipos() {
    return [
      { codigo: 'VAREJO', descricao: 'Varejo' },
      { codigo: 'FOODSERVICE', descricao: 'Food Service' }
    ];
  }
}
