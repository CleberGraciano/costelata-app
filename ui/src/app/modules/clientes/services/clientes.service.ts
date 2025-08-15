import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '@core/services/api.service';
import { Observable, of } from 'rxjs';
import { PaginacaoResponse, ClienteResponse, Cliente } from '../models/clientes.models';

const baseRef = '/omie/clientes';
const clientes = `/clientes`;

const routes = {
  filterOmie: `${baseRef}`,
  filterBase: `${clientes}/paginado`,
  filterByIdOmie: `${baseRef}/buscar`,
  insert: `${clientes}`,
  edit: `${clientes}`,
  delete: (id: number) => `${clientes}/${id}`,
  statusEdit: `${clientes}/alterar-status`,
  aprovarCadastro: `${clientes}/aprovar-omie`,
};

@Injectable({
  providedIn: 'root'
})
export class ClientesService {
  constructor(private apiService: ApiService) { }

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

    if (obj.omie) {
      return this.apiService.get<PaginacaoResponse<ClienteResponse>>(`${routes.filterOmie}`, false, params);
    } else {
      return this.apiService.get<PaginacaoResponse<ClienteResponse>>(`${routes.filterBase}`, false, params);
    }
  }

  itemById(obj: any) {
    let params = new HttpParams();
    params = params.append('codigo_cliente_omie', obj.codigo_cliente_omie);
    params = params.append('codigo_cliente_integracao', obj.codigo_cliente_integracao);
    return this.apiService.get<any>(routes.filterByIdOmie, false, params);
  }

  insert(obj: any) {
    return this.apiService.post<any>(routes.insert, obj);
  }

  statusEdit(obj: any) {
    let params = new HttpParams();
    // if (obj.codigo_cliente_omie) {
    //   params = params.append('codigo_cliente_omie', obj.codigo_cliente_omie);
    // } else if (obj.codigo_cliente_integracao) {
    //   params = params.append('codigo_cliente_integracao', obj.codigo_cliente_integracao);
    // }
    // params = params.append('status', obj.status);
    return this.apiService.put<any>(routes.statusEdit, obj);
  }

  aprovarUsuario(cliente: any) {
    return this.apiService.post<any>(routes.aprovarCadastro, cliente);
  }

  edit(obj: any) {
    let params = new HttpParams();
    if (obj.codigo_cliente_omie) {
      params = params.append('codigo_cliente_omie', obj.codigo_cliente_omie);
    } else if (obj.codigo_cliente_integracao) {
      params = params.append('codigo_cliente_integracao', obj.codigo_cliente_integracao);
    }
    return this.apiService.put<any>(routes.edit, obj, false, params);
  }

  delete(id: number) {
    return this.apiService.delete(routes.delete(id));
  }
}
