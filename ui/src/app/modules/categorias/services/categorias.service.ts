import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '@core/services/api.service';
import { PaginacaoResponse, ClienteResponse } from '@modules/clientes/models/clientes.models';
import { Observable } from 'rxjs';

const baseRef = '/categorias';
const routes = {
  filter: `${baseRef}`,
  filterById: (id: number) => `${baseRef}/${id}`,
  insert: `${baseRef}`,
  edit: (id: number) => `${baseRef}/${id}`,
  alterarStatus: (id: number, status: string) => `${baseRef}/${id}/alterar-status?status=${status}`,
  combo: `${baseRef}/combo`
}

@Injectable({
  providedIn: 'root'
})
export class CategoriasService {

  constructor(
    private apiService: ApiService
  ) { }

  filter(obj: any): Observable<PaginacaoResponse<ClienteResponse>> {
    let params = new HttpParams();
    for (const key in obj) {
      if (
        obj.hasOwnProperty(key) &&
        obj[key] !== null &&
        obj[key] !== undefined &&
        obj[key] !== ''
      ) {
        if (key != 'omie') {
          params = params.append(key, obj[key]);
        }
      }
    }
    params = params.append('page', obj.page ?? 0);
    params = params.append('size', obj.size ?? 10);

    return this.apiService.get<PaginacaoResponse<ClienteResponse>>(`${routes.filter}`, false, params);
  }

  itemById(id: number) {
    return this.apiService.get<any>(routes.filterById(id));
  }

  insert(obj: any) {
    return this.apiService.post<any>(routes.insert, obj);
  }

  edit(id: number, obj: any) {
    return this.apiService.put<any>(routes.edit(id), obj);
  }

  alterarStatus(id: number, status: string) {
    return this.apiService.patch<any>(routes.alterarStatus(id, status));
  }

  combo(): Observable<any> {
    return this.apiService.get<any>(routes.combo);
  }
}
