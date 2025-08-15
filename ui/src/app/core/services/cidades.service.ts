import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';

const baseRef = '/omie/cidades';
const routes = {
  filter: `${baseRef}`,
};

@Injectable({
  providedIn: 'root'
})
export class CidadesService {

  constructor(private apiService: ApiService) { }

  listarCidades(filtros: any = {}, pagina: number = 1, registrosPorPagina: number = 1000) {
    let params = new HttpParams();
    for (const key in filtros) {
      if (
        filtros.hasOwnProperty(key) &&
        filtros[key] !== null &&
        filtros[key] !== undefined &&
        filtros[key] !== ''
      ) {
        params = params.append(key, filtros[key]);
      }
    }
    params = params.append('pagina', pagina);
    params = params.append('registrosPorPagina', registrosPorPagina);
    return this.apiService.get(`${routes.filter}`, false, params);
  }
}
