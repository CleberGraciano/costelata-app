import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Usuario } from '@modules/usuarios/models/usuarios.models';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import { TableLazyLoadEvent } from 'primeng/table';
import { UsuariosService } from '@modules/usuarios/services/usuarios.service';
import { StatusService } from '@core/services/status.service';
import { EstadosService } from '@core/services/estados.service';
import { PerfilService } from '@core/services/perfil.service';
import { CidadesService } from '@core/services/cidades.service';

@Component({
  selector: 'app-usuarios-list',
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent extends BaseListComponent<Usuario> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  usuarios: Usuario[] = [];
  carregando = false;
  cnpjCpfMask = '000.000.000-00 || 00.000.000/0000-00';

  modalCadastroAprovadoVisivel = false;
  modalCadastroAprovadoUsuario?: Usuario;

  service = inject(UsuariosService);
  override estados = inject(EstadosService).listarEstados();
  override status = inject(StatusService).listarStatus();
  override perfis = inject(PerfilService).listarPerfis();
  cidadesService = inject(CidadesService);

  ngOnInit(): void {
    this.inicializarFormulario();
    this.pesquisar();
  }

  ngAfterViewInit(): void {
    this.setHeader('Usuários', 'usuarios');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      email: [''],
      userRole: [undefined],
      razaoSocial: [''],
      nomeFantasia: [''],
      estado: [undefined],
      cidade: [undefined],
      status: [undefined]
    });

    this.carregarCidades();
  }

  carregarCidades() {
    this.estadoControl.valueChanges.subscribe((estado: any) => {
      if (estado) {
        this.cidadesService.listarCidades({ uf: estado.codigo }).subscribe((dados: any) => {
          this.cidades = dados;
        });
      } else {
        this.cidades = [];
      }
    });
  }

pesquisar(): void {
    this.carregando = true;
    this.filtro = this.filtroForm.getRawValue();

    this.filtro.cidade = this.filtro.cidade?.cNome;
    this.filtro.estado = this.filtro.estado?.codigo;
    this.filtro.status = this.filtro.status?.codigo;

    this.service.filter(this.filtro)
      .subscribe({
        next: (dados: any) => {
          this.usuarios = dados.content;
          this.totalRecords = dados.totalElements ?? dados.totalRecords ?? 0;
        },
        error: (error: any) => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar usuários.' });
        },
        complete: () => {
          this.carregando = false;
        }
      });
  }

  limparFiltro(): void {
    this.filtroForm.reset();
    this.pesquisar();
  }

  editar(usuario: Usuario): void {
    this.router.navigate(['/usuarios/editar', usuario.id]);
  }

  alterarStatus(usuario: Usuario): void {
    const message = `Deseja realmente ${usuario.status === 'Ativado' ? 'bloquear' : 'ativar'} o usuário <strong>${usuario.nomeFantasia}</strong>?`;

    if(usuario.id) {
      this.dialogServiceApp.confirm(message, () => {
        (usuario.status === 'Ativado')
          ? this.inativarCallBack(usuario)
          : this.ativarCallBack(usuario);
      });
    }
  }

  ativarCallBack(usuario: Usuario) {
    this.service.ativar(usuario.id).subscribe({
      next: (res: any) => {
        this.messageService?.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário ativado com sucesso.' });
        this.pesquisar();
      },
      error: () => {
        this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao ativar usuário.' });
      }
    });
  }

  inativarCallBack(usuario: Usuario) {
    this.service.inativar(usuario.id).subscribe({
      next: (res: any) => {
        this.messageService?.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário inativado com sucesso.' });
        this.pesquisar();
      },
      error: () => {
        this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao inativar usuário.' });
      }
    });
  }

  aprovarCadastro(usuario: Usuario) {
    const message = `Deseja realmente aprovar o cadastro do usuário <strong>${usuario.nomeFantasia}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.service.aprovarCadastro(usuario.id).subscribe({
        next: (res: any) => {
          this.abrirModalCadastroAprovado(res);
        },
        error: () => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao aprovar cadastro do usuário.' });
        }
      });
    });
    // this.abrirModalCadastroAprovado({
    //   ...usuario,
    //   senhaProvisoria: usuario.senhaProvisoria || 'senha1234'
    // });
  }

  abrirModalCadastroAprovado(usuario: Usuario) {
    this.modalCadastroAprovadoUsuario = usuario;
    this.modalCadastroAprovadoVisivel = true;
  }  

  onLazyLoad(event: TableLazyLoadEvent) {
    const page = (event.first ?? 0) / (event.rows ?? this.rows);
    this.rows = event.rows ?? this.rows;
    this.filtro = { ...this.filtro, page, rows: this.rows };
    this.pesquisar();
  }

  get estadoControl() {
    return this.filtroForm.get('estado') as FormControl<string>;
  }

}
