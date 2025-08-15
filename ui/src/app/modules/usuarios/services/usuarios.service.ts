import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '@core/services/api.service';
import { PaginacaoResponse, ClienteResponse } from '@modules/clientes/models/clientes.models';
import { Observable, of } from 'rxjs';
import { Usuario } from '../models/usuarios.models';

const baseRef = '/users';

const routes = {
    filter: `${baseRef}/paginado`,
    filterById: (id: number) => `${baseRef}/${id}`,
    insert: `${baseRef}`,
    edit: (id: number) => `${baseRef}/${id}`,
    delete: (id: number) => `${baseRef}/${id}`,
    ativar: (id: number) => `${baseRef}/${id}/ativar`,
    inativar: (id: number) => `${baseRef}/${id}/inativar`,
    reenviarSenha: (id: number) => `${baseRef}/${id}/renovar-senha-provisoria`,
    aprovarCadastro: (id: number) => `${baseRef}/aprovar-cadastro/${id}`,
    redefinirSenha: `${baseRef}/redefinir-senha`
};

@Injectable({ providedIn: 'root' })
export class UsuariosService {
    constructor(private apiService: ApiService) { }

    public usuarios: any[] = [
        { id: 1, nome: 'Administrador', email: 'admin@costelata.com', role: 'Admin', status: 'ATIVO' },
        { id: 2, nome: 'Usuário Comum', email: 'user@costelata.com', role: 'Comum', status: 'INATIVO' }
    ];

    getAll(): Observable<Usuario[]> {
        return of(this.usuarios);
    }

    filter(obj: any): Observable<PaginacaoResponse<ClienteResponse>> {
        let params = new HttpParams();
        for (const key in obj) {
            if (
                obj.hasOwnProperty(key) &&
                obj[key] !== null &&
                obj[key] !== undefined &&
                obj[key] !== ''
            ) {
                params = params.append(key, obj[key]);
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

    ativar(usuarioId: number) {
        return this.apiService.patch<any>(routes.ativar(usuarioId));
    }

    inativar(usuarioId: number) {
        return this.apiService.patch<any>(routes.inativar(usuarioId));
    }

    edit(id: number, obj: any) {
        return this.apiService.put<any>(routes.edit(id), obj);
    }

    delete(id: number) {
        return this.apiService.delete(routes.delete(id));
    }

    reenviarSenha(usuarioId: number) {
        return this.apiService.patch<any>(routes.reenviarSenha(usuarioId));
    }

    aprovarCadastro(usuarioId: any) {
        return this.apiService.get<any>(routes.aprovarCadastro(usuarioId));
    }

    redefinirSenha(obj: any) {
        return this.apiService.post<any>(routes.redefinirSenha, obj);
    }
}
