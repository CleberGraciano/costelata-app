import { AuthenticationService } from '@core/auth/authentication.service';
import { LocalStorageService } from '@core/local-storage/local-storage.service';
import { Cliente } from './../../modules/clientes/models/clientes.models';
import { ApiService } from '@core/services/api.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { BehaviorSubject, catchError, map, Observable, throwError, switchMap } from 'rxjs';
import { NgxPermissionsService } from 'ngx-permissions';
import { PermissaoEnum } from '@core/enum/permissao.enum';

const URL_BASE_AUTH = environment.apiAuth;
const URL_BASE = environment.api;

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(
    private authenticationService: AuthenticationService,
    private localStorageService: LocalStorageService,
    private permissionService: NgxPermissionsService,
    private apiService: ApiService
  ) { }

  authUser(email: string, password: string): Observable<any> {
    return this.apiService.post(`${URL_BASE_AUTH}/login`, { email, password }).pipe(
      map((loginRes: any) => {
        this.localStorageService.setItem('token', loginRes?.token);
        return loginRes;
      }),
      switchMap((loginRes: any) =>
        this.apiService.get(`${URL_BASE_AUTH}/prefs`).pipe(
          map((prefs: any) => {
            const usuarioLogado = prefs;
            const permissoes = usuarioLogado?.role;
            this.authenticationService.setUsuarioLogado(prefs);

            if (permissoes) {
              const permissoesArray = Array.isArray(permissoes) ? permissoes : [permissoes];
              this.permissionService.loadPermissions(permissoesArray);
            }
            return { login: loginRes, prefs };
          })
        )
      ),
      catchError((error: any) => {
        return throwError(error);
      })
    );
  }

  solicitarCadastro(cliente: Cliente) {
    return this.apiService.post<any>(`${URL_BASE}/clientes/solicitar-cadastro`, cliente);
  }
}
