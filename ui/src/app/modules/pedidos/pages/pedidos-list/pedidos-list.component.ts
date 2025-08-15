import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PedidosService } from '../../services/pedidos.service';
import { Pedido } from '../../models/pedidos.model';
import { Router } from '@angular/router';
import { HeaderSignalService } from 'src/app/header-signal.service';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import { TableLazyLoadEvent } from 'primeng/table';
import { StatusService } from '@core/services/status.service';

@Component({
  selector: 'app-pedidos-list',
  templateUrl: './pedidos-list.component.html',
  styleUrls: ['./pedidos-list.component.css']
})
export class PedidosListComponent extends BaseListComponent<Pedido> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  pedidos: Pedido[] = [];
  override totalRecords = 0;
  carregando = false;
  cnpjCpfMask = '000.000.000-00 || 00.000.000/0000-00';

  service = inject(PedidosService);
  statusPedido = inject(StatusService).listarStatusPedidos();

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  ngAfterViewInit(): void {
    this.setHeader('Pedidos', 'pedidos');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      numeroPedido: [''],
      nomeFantasia: [''],
      status: [undefined],
      // plataforma: [undefined]
    });
  }

  pesquisar(): void {
    this.carregando = true;
    this.filtro = this.filtroForm.getRawValue();

    this.filtro.status = this.filtro.status?.codigo;

    this.service.filter(this.filtro)
      .subscribe({
        next: (dados: any) => {
          this.pedidos = dados.content;
          this.totalRecords = dados.totalElements ?? dados.totalRecords ?? 0;
        },
        error: (error: any) => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar pedidos.' });
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

  editar(cliente: Pedido): void {
    // this.router.navigate(['/pedidos/editar', pedido.id]);

    // const queryParams = Object.fromEntries(
    //   Object.entries({
    //     codigo_cliente_integracao: cliente.codigo_cliente_integracao ?? cliente.codigoClienteIntegracao,
    //     codigo_cliente_omie: cliente.codigo_cliente_omie
    //   }).filter(([_, v]) => v != null)
    // );
    // this.router.navigate([
    //   '/clientes/editar'
    // ], {
    //   queryParams
    // });
  }

  alterarStatus(pedido: Pedido): void {
    const message = `Deseja realmente ${pedido.status === 'Ativo' ? 'bloquear' : 'ativar'} o pedido <strong>${pedido}</strong>?`;

    const isBloqueado = pedido.status === 'Bloqueado';
    const novoStatus = isBloqueado ? 'Ativo' : 'Bloqueado';

    const objEnvio: any = {
      status: novoStatus,
      id: pedido.id
      // codigo_cliente_omie: cliente.codigo_cliente_omie || undefined,
      // codigo_cliente_integracao: cliente.codigo_cliente_integracao || undefined
    };

    // if (!objEnvio.codigo_cliente_omie) delete objEnvio.codigo_cliente_omie;
    // if (!objEnvio.codigo_cliente_integracao) delete objEnvio.codigo_cliente_integracao;

    this.dialogServiceApp.confirm(message, () => {
      this.service.statusEdit(objEnvio).subscribe({
        next: () => {
          this.messageService?.add({ severity: 'success', summary: 'Sucesso', detail: `Pedido ${novoStatus} com sucesso.` });
          this.pesquisar();
        },
        error: () => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao alterar status do pedido.' });
        }
      });
    });
  }

  onLazyLoad(event: TableLazyLoadEvent) {
    const page = (event.first ?? 0) / (event.rows ?? this.rows);
    this.rows = event.rows ?? this.rows;
    this.filtro = { ...this.filtro, page, rows: this.rows };
    this.pesquisar();
  }
}