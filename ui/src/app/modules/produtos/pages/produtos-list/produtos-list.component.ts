import { AfterViewInit, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { RouteAction } from '@core/enum/actions.enum';
import { DialogServiceApp } from '@core/services/dialog.service';
import { StatusService } from '@core/services/status.service';
import { CategoriasService } from '@modules/categorias/services/categorias.service';
import { Produto } from '@modules/produtos/models/produtos.model';
import { ProdutosService } from '@modules/produtos/services/produtos.service';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import type { TableLazyLoadEvent } from 'primeng/table';

@Component({
  selector: 'app-produtos-list',
  templateUrl: './produtos-list.component.html'
})
export class ProdutosListComponent extends BaseListComponent<Produto> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  produtos: Produto[] = [];
  override totalRecords = 0;
  carregando = false;
  cnpjCpfMask = '000.000.000-00 || 00.000.000/0000-00';
  categorias: any;

  service = inject(ProdutosService);
  serviceCategoria = inject(CategoriasService);
  override status = inject(StatusService).listarStatus();

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  ngAfterViewInit(): void {
    this.setHeader('Produtos', 'produtos');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      codigo: [''],
      descricao: [''],
      categoria: [null],
      status: [null]
      // codigoCategoria: [null],
      // valorMinimo: [null],
      // valorMaximo: [null],
      // quantidadeMinima: [null],
      // quantidadeMaxima: [null]
    });

    this.carregarSelects();
  }

  carregarSelects(): void {
    this.serviceCategoria.combo().subscribe((dados) => {
      this.categorias = dados;
    });
  }

  pesquisar(): void {
    this.carregando = true;
    // this.filtro = this.filtroForm.getRawValue();
    this.filtro.status = this.filtro.status?.codigo ?? null;

    this.service.filter(this.filtro)
      .subscribe({
        next: (dados: any) => {
          this.produtos = dados.content;
          this.totalRecords = dados.totalElements ?? dados.totalRecords ?? 0;
        },
        error: (error: any) => {
          this.messageService?.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar produtos.' });
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

  editar(produto: Produto): void {
    const queryParams = Object.fromEntries(
      Object.entries({
        codigo_produto_integracao: produto?.codigo_produto_integracao ?? produto?.codigoProdutoIntegracao,
        codigo_produto: produto?.codigo_produto
      }).filter(([_, v]) => v != null)
    );
    this.router.navigate([
      '/produtos/editar'
    ], {
      queryParams
    });
  }

  alterarStatus(produto: Produto): void {
    const message = `Deseja realmente ${produto.status === 'Ativo' ? 'bloquear' : 'ativar'} o produto <strong>${produto}</strong>?`;

    const isBloqueado = produto.status === 'Bloqueado';
    const novoStatus = isBloqueado ? 'Ativo' : 'Bloqueado';

    const objEnvio: any = {
      status: novoStatus,
      id: produto.id
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
    console.log('Lazy load event:', event);
    this.pesquisar();
  }
}
