import { Component, inject } from '@angular/core';
import { Validators } from '@angular/forms';
import { emailCustomValidator } from 'src/app/shared/validators/email-validator';
import { SharedModule } from '@shared/shared.module';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';
import { AuthService } from '@core/services/auth.service';
import { Cliente } from '@modules/clientes/models/clientes.models';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css'],
  standalone: true,
  imports: [
    SharedModule
  ]
})
export class CadastroComponent extends BaseFormComponent<any> {

  authService = inject(AuthService);
  modalCadastroSucessoVisivel = false;

  protected buildForm(): void {
    this.form = this.fb.group({
      nomeFantasia: ['', [Validators.required, Validators.minLength(3)]],
      razaoSocial: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email, emailCustomValidator]]
    });
  }
  
  cadastrar() {
    const objEnvio = { ...this.form.getRawValue() };
    const formValid = this.salvar(objEnvio);

    if (formValid) {
      this.authService.solicitarCadastro(objEnvio).subscribe({
        next: (response: Cliente) => {
          this.modalCadastroSucessoVisivel = true;
        },
        error: (error: any) => {
          this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Erro ao solicitar cadastro!' });
        }
      });
    }
  }

  irParaLogin() {
    this.router.navigate(['/login']);
  }
  
  protected buscarDados(id: number): void { }
}
