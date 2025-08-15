import { AuthenticationService } from '@core/auth/authentication.service';
import { Component, inject } from '@angular/core';
import { Validators } from '@angular/forms';
import { SharedModule } from '@shared/shared.module';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [SharedModule]
})
export class LoginComponent extends BaseFormComponent<any> {
  
  authenticationService = inject(AuthenticationService);
  authService = inject(AuthService);
  mostrarSenha = false;

  protected override buildForm(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  login() {
    const objEnvio = { ...this.form.getRawValue() };
    const formValid = this.salvar(objEnvio);

    if (formValid) {
      this.authService.authUser(objEnvio.email, objEnvio.password).subscribe({
        next: (res) => {
          this.authenticationService.setUsuarioLogado(res.prefs);
          const roles = res.prefs.role;
          if (roles.includes('USER')) {
            this.router.navigate(['/pedidos/fazer-meu-pedido']);
          } else {
            this.irParaHome();
          }
        },
        error: (error: any) => {
          this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'E-mail ou senha inválidos.' });
        }
      });
    }
  }

  irParaHome() {
    this.router.navigate(['/']);
  }

  protected override buscarDados(id: number): void { }
}