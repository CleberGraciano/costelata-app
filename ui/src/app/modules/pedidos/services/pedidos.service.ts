import { Injectable } from '@angular/core';
import { Pedido } from '../models/pedidos.model';
import { Observable, of } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { ApiService } from '@core/services/api.service';
import { PaginacaoResponse } from '@modules/clientes/models/clientes.models';

const baseRef = '/omie/pedidos';
const pedidos = `/pedidos`;

const routes = {
  filterOmie: `${baseRef}/paginado`,
  filterBase: `${pedidos}/paginado`,
  filterById: `${pedidos}`,
  insert: `${pedidos}`,
  edit: `${pedidos}`,
  statusEdit: `${pedidos}/alterar-status`
};

@Injectable({ providedIn: 'root' })
export class PedidosService {
  constructor(private apiService: ApiService) { }

  filter(obj: any): Observable<PaginacaoResponse<Pedido>> {
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

    return this.apiService.get<PaginacaoResponse<Pedido>>(`${routes.filterOmie}`, false, params);
    // if (obj.omie) {
    //   return this.apiService.get<PaginacaoResponse<Pedido>>(`${routes.filterOmie}`, false, params);
    // } else {
    //   return this.apiService.get<PaginacaoResponse<Pedido>>(`${routes.filterBase}`, false, params);
    // }
  }

  itemById(obj: any) {
    let params = new HttpParams();
    if (obj.codigo_cliente_omie) {
      params = params.append('codigo_cliente_omie', obj.codigo_cliente_omie);
    } else if (obj.codigo_cliente_integracao) {
      params = params.append('codigo_cliente_integracao', obj.codigo_cliente_integracao);
    }
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
}
