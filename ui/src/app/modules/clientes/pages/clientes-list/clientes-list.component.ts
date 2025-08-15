import { UsuariosService } from '@modules/usuarios/services/usuarios.service';
import type { TableLazyLoadEvent } from 'primeng/table';
import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Cliente, ClienteResponse } from '../../models/clientes.models';
import { ClientesService } from '@modules/clientes/services/clientes.service';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import { CidadesService } from '@core/services/cidades.service';
import { EstadosService } from '@core/services/estados.service';
import { PerfilService } from '@core/services/perfil.service';
import { StatusService } from '@core/services/status.service';

@Component({
  selector: 'app-clientes-list',
  templateUrl: './clientes-list.component.html',
  styleUrls: ['./clientes-list.component.css']
})
export class ClientesListComponent extends BaseListComponent<Cliente> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  clientes: Cliente[] = [];
  override totalRecords = 0;
  carregando = false;
  cnpjCpfMask = '000.000.000-00 || 00.000.000/0000-00';

  plataforma: any[] = [
    { codigo: 'omie', descricao: 'Omie' },
    { codigo: 'costelata', descricao: 'Costelata' },
  ];

  modalCadastroReenvioSenhaVisivel = false;
  modalCadastroReenvioSenha?: Cliente;

  modalCadastroAprovadoVisivel = false;
  modalCadastroAprovadoUsuario?: Cliente;


  service = inject(ClientesService);
  usuariosService = inject(UsuariosService);
  override estados = inject(EstadosService).listarEstados();
  override status = inject(StatusService).listarStatus();
  override perfis = inject(PerfilService).listarPerfis();
  cidadesService = inject(CidadesService);

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  ngAfterViewInit(): void {
    this.setHeader('Clientes', 'clientes');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      email: [''],
      razaoSocial: [''],
      nomeFantasia: [''],
      estado: [undefined],
      cidade: [undefined],
      status: [undefined],
      plataforma: [undefined]
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

    const filtroSemPlataforma = { ...this.filtro };
    delete filtroSemPlataforma.plataforma;
    
    let algumCampoPreenchido = Object.values(filtroSemPlataforma).some(v => v !== undefined && v !== null && v !== '');
    let plataformaOmie = this.filtro.plataforma?.codigo === 'omie';
    let plataformaCostelata = this.filtro.plataforma?.codigo === 'costelata';

    this.filtro.cidade = this.filtro.cidade?.cNome;
    this.filtro.estado = this.filtro.estado?.codigo;
    this.filtro.status = this.filtro.status?.codigo;
    this.filtro.plataforma = this.filtro.plataforma?.codigo;

    this.filtro.omie = true;

    if(algumCampoPreenchido || plataformaCostelata) {
      this.filtro.omie = null;
    }

    if(plataformaOmie && algumCampoPreenchido) {
      this.filtro.omie = null;
    }

    this.service.filter(this.filtro)
      .subscribe({
        next: (dados: any) => {
          this.clientes = dados.content;
          this.totalRecords = dados.totalElements ?? dados.totalRecords ?? 0;
        },
        error: (error: any) => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar clientes.' });
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

  editar(cliente: ClienteResponse): void {
    const queryParams = Object.fromEntries(
      Object.entries({
        codigo_cliente_integracao: cliente.codigo_cliente_integracao ?? cliente.codigoClienteIntegracao,
        codigo_cliente_omie: cliente.codigo_cliente_omie
      }).filter(([_, v]) => v != null)
    );
    this.router.navigate([
      '/clientes/editar'
    ], {
      queryParams
    });
  }

  alterarStatus(cliente: Cliente): void {
    const message = `Deseja realmente ${cliente.status === 'Ativo' ? 'bloquear' : 'ativar'} o cliente <strong>${cliente.nome_fantasia ?? cliente.nomeFantasia}</strong>?`;

    const isBloqueado = cliente.status === 'Bloqueado';
    const novoStatus = isBloqueado ? 'Ativo' : 'Bloqueado';

    const objEnvio: any = {
      status: novoStatus,
      id: cliente.id
      // codigo_cliente_omie: cliente.codigo_cliente_omie || undefined,
      // codigo_cliente_integracao: cliente.codigo_cliente_integracao || undefined
    };

    // if (!objEnvio.codigo_cliente_omie) delete objEnvio.codigo_cliente_omie;
    // if (!objEnvio.codigo_cliente_integracao) delete objEnvio.codigo_cliente_integracao;

    this.dialogServiceApp.confirm(message, () => {
      this.service.statusEdit(objEnvio).subscribe({
        next: () => {
          this.messageService?.add({ severity: 'success', summary: 'Sucesso', detail: `Cliente ${novoStatus} com sucesso.` });
          this.pesquisar();
        },
        error: () => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao alterar status do cliente.' });
        }
      });
    });
  }

  aprovarCadastro(cliente: Cliente) {
    const message = `Deseja realmente aprovar o cadastro do cliente <strong>${cliente.nome_fantasia ?? cliente.nomeFantasia}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.service.aprovarUsuario(cliente).subscribe({
        next: (res: any) => {
          this.abrirModalCadastroAprovado(res);
        },
        error: () => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao aprovar cadastro do cliente.' });
        }
      });
    });
    // this.abrirModalCadastroAprovado({
    //   ...cliente,
    //   senhaProvisoria: cliente.senhaProvisoria || 'senha1234'
    // });
  }

  abrirModalCadastroAprovado(cliente: Cliente) {
    this.modalCadastroAprovadoUsuario = cliente;
    this.modalCadastroAprovadoVisivel = true;
  }

  reenviarSenhaAcesso(cliente: Cliente) {
    const message = `Deseja realmente criar uma nova senha provisória para o cliente <strong>${cliente.nome_fantasia ?? cliente.nomeFantasia}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.usuariosService.reenviarSenha(cliente.idUser).subscribe({
        next: (res: any) => {
          this.abrirModalReenviarSenhaAcesso(res);
        },
        error: () => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao reenviar senha provisória.' });
        }
      });
    });
    // this.abrirModalReenviarSenhaAcesso({
    //   ...cliente,
    //   senhaProvisoria: cliente.senhaProvisoria || 'senha1234'
    // });
  }

  abrirModalReenviarSenhaAcesso(cliente: Cliente) {
    this.modalCadastroReenvioSenhaVisivel = true;
    this.modalCadastroReenvioSenha = cliente;
  }

  onLazyLoad(event: TableLazyLoadEvent) {
    const page = (event.first ?? 0) / (event.rows ?? this.rows);
    this.rows = event.rows ?? this.rows;
    this.filtro = { ...this.filtro, page, rows: this.rows };
    this.pesquisar();
  }

  validacaoCadastro(cliente: ClienteResponse): string {
    if(cliente.codigo_cliente_integracao || cliente.codigoClienteIntegracao) {
      return 'Costelata';
    }
    else if(cliente.codigo_cliente_omie) {
      return 'Omie';
    }
    else {
        return '-';
    }
  }

  get estadoControl() {
    return this.filtroForm.get('estado') as FormControl<string>;
  }
}
