import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '@core/services/api.service';
import { Produto } from '../models/produtos.model';
import { PaginacaoResponse } from '@modules/clientes/models/clientes.models';
import { Observable } from 'rxjs';

const baseRef = '/omie/produtos';
const produtos = `/produtos`;

const routes = {
  filterOmie: `${baseRef}/paginado`,
  filterBase: `${produtos}/paginado`,
  filterById: `${baseRef}/buscar`,
  insert: `${produtos}`,
  edit: `${produtos}`,
  statusEdit: `${produtos}/alterar-status`,
  delete: (id: number) => `${produtos}/${id}`
}

@Injectable({
  providedIn: 'root'
})
export class ProdutosService {

  constructor(private apiService: ApiService) { }

  filter(obj: any): Observable<PaginacaoResponse<Produto>> {
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

    console.log(obj);
    params = params.append('page', obj.page ?? 0);
    params = params.append('size', obj.size ?? 10);

    return this.apiService.get<PaginacaoResponse<Produto>>(`${routes.filterOmie}`, false, params);
    // if (obj.omie) {
    //   return this.apiService.get<PaginacaoResponse<Produto>>(`${routes.filterOmie}`, false, params);
    // } else {
    //   return this.apiService.get<PaginacaoResponse<Produto>>(`${routes.filterBase}`, false, params);
    // }
  }

  itemById(obj: any) {
    let params = new HttpParams();
    params = params.append('codigo_produto', obj.codigo_produto);
    params = params.append('codigo_produto_integracao', obj.codigo_produto_integracao);
    return this.apiService.get<any>(routes.filterById, false, params);
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
