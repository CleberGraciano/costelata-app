import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { delay } from 'rxjs/operators';
import { LocalStorageService } from '@core/local-storage/local-storage.service';
import { Router } from '@angular/router';
import { environment } from '@env/environment';

const routes = {
  login: `signin`,
  forgotenPassword: (email: string) => `${email}/forgotpasswd`,
  changePassword: `user/changepasswd`
}

@Injectable({ providedIn: 'root' })
export class AuthenticationService {

  public loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  public isLoggedIn$ = this.loggedIn.asObservable();

  constructor(
    private localStorageService: LocalStorageService,
    private router: Router
  ) { }

  // Remove getter isLoggedIn (não é necessário)

  public getUsuarioLogado() {
    return this.localStorageService.getItem('usuario');
  }

  setUsuarioLogado(usuario: any) {
    this.localStorageService.setItem('usuario', usuario);
    // Considera logado se o usuário não for null/undefined
    this.loggedIn.next(!!usuario);
  }

  logout() {
    this.loggedIn.next(false); // Atualiza o estado de login
    this.localStorageService.clear();
    this.irParaLogin();
  }

  irParaLogin() {
    this.router.navigate(['/login']);
  }

  private hasToken(): boolean {
    // Considera logado se existir token no localStorage
    return !!this.localStorageService.getItem('token');
  }
}