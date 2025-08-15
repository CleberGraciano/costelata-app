import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {

  constructor() { }

  listarPerfis() {
    return [
      { "codigo": "ADMIN", "descricao": "Administrador" },
      { "codigo": "USER", "descricao": "Usuário" }
    ];
  }
}
