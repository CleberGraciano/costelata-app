import { Component, AfterViewInit, inject } from '@angular/core';
import { Validators } from '@angular/forms';
import { emailCustomValidator } from 'src/app/shared/validators/email-validator';
import { Usuario } from '../../models/usuarios.models';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';
import { PerfilService } from '@core/services/perfil.service';
import { UsuariosService } from '@modules/usuarios/services/usuarios.service';

@Component({
  selector: 'app-usuarios-form',
  templateUrl: './usuarios-form.component.html',
  styleUrls: ['./usuarios-form.component.css']
})
export class UsuariosFormComponent extends BaseFormComponent<Usuario> implements AfterViewInit {

  service = inject(UsuariosService);
  override perfis = inject(PerfilService).listarPerfis();

  protected buildForm(): void {
    this.form = this.fb.group({
      nomeFantasia: ['', Validators.required],
      telefone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, emailCustomValidator]],
      role: [undefined]
    });
  }

  protected buscarDados(): void {
    console.log('idParams', this.idParams);
    this.service.itemById(this.idParams).subscribe((usuario: Usuario) => {
      this.form.patchValue(usuario);
    });
  }

  salvarUsuario(): void {
    const usuario: Usuario = this.form.getRawValue();
    const formValid = this.salvar(usuario);

    usuario.role = usuario.role ? usuario.role?.codigo : 'User';

    console.log('Form Valid:', formValid);
    if (formValid) {
      if (this.idParams)
        this.callBackEditar(usuario)
      else
        this.callBackCriar(usuario);
    }
  }

  callBackCriar(usuario: Usuario): void {
    console.log(usuario)
    this.service.insert(usuario).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário criado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar usuário.' });
        // console.error('Error creating user:', err);
      }
    });
  }

  callBackEditar(usuario: Usuario): void {
    this.service.edit(this.idParams, usuario).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário editado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao editar usuário.' });
        // console.error('Error editing user:', err);
      }
    });
  }

  irParaLista(): void {
    this.router.navigate(['/usuarios']);
  }

  ngAfterViewInit(): void {
    this.setHeader('Usuários', 'usuarios');
  }
}
