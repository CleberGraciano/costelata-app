import { Directive, ElementRef, inject, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { RouteAction } from '@core/enum/actions.enum';
import { FormService } from '@core/services/form.service';
import { MessageService } from 'primeng/api';
import { HeaderSignalService } from 'src/app/header-signal.service';
import { DialogServiceApp } from '@core/services/dialog.service';

@Directive()
export abstract class BaseListComponent<T> implements OnDestroy {
  @ViewChild('formElement') formElement!: ElementRef;

  form!: FormGroup;
  idParams!: number;
  acao!: RouteAction;
  filtro!: any;
  isMinhaConta!: boolean;

  perfis: { codigo: string; descricao: string; }[] | undefined;
  status: { codigo: string; descricao: string; }[] | undefined;
  estados: { codigo: string; descricao: string; }[] | undefined;
  cidades: any[] = [];

  rows = 10;
  totalRecords = 0;

  protected destroy$ = new Subject<void>();

  constructor(
    protected fb: FormBuilder,
    protected router: Router,
    protected activatedRoute: ActivatedRoute,
    protected formService: FormService,
    protected messageService: MessageService,
    protected headerSignal: HeaderSignalService,
    protected dialogServiceApp: DialogServiceApp
  ) {
    this.setParametros();
  }

  protected setHeader(titulo: string, rotaBase: string, acao?: RouteAction): void {
    const acaoAtualValor = acao ?? RouteAction.Cadastrar;
    this.headerSignal.title.set(titulo);
    this.headerSignal.actionPage.set(acaoAtualValor);
    this.headerSignal.breadcrumbs.set([
      { label: 'Início', url: '/' },
      { label: titulo, url: `/${rotaBase}` },
      { label: `${acaoAtualValor} ${titulo.slice(0, -1)}`, url: `/${rotaBase}/${acaoAtualValor}` }
    ]);
    const acaoBtn = {
      label: `Novo ${titulo}`,
      icon: 'pi pi-plus',
      url: `/${rotaBase}/${RouteAction.Cadastrar}`,
    };
    const ativarBtn = rotaBase ? acaoBtn : null;
    this.headerSignal.actionButton.set(ativarBtn);
  }

  protected buscarDados(service: any) {
    return service.subscribe((dados: any) => {
      return dados;
    });
  };

  protected setParametros(): void {
    const rotaBase = this.activatedRoute.snapshot.pathFromRoot.map(route => route.routeConfig?.path).filter(Boolean)[0];
    if (rotaBase === 'minha-conta') {
      this.router.navigate(['/minha-conta/configuracoes']);
    }
  }

  editarStatus(service: any, message: string): void {
    this.dialogServiceApp.confirm(message, () => {
      service
        .subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Status alterado com sucesso!' });
            this.buscarDados(service.listar);
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao alterar status!' });
          }
        });
    });
  }

  salvar(objEnvio: T): boolean {
    const messages = this.formService.getValidationsMessages(this.form, this.formElement);
    if (messages.length) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Existem campos obrigatórios a serem preenchidos.' });
      return false;
    }

    if (this.form.invalid) return false;

    return true;
  }

  voltar(): void {
    let parent = this.activatedRoute.parent;
    while (parent?.routeConfig?.path === '') {
      parent = parent.parent;
    }

    const rotaBase = parent?.routeConfig?.path?.split('/')[0] || '';
    this.router.navigate(['/' + rotaBase]);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  compararStatus = (option: any, value: any) => {
    if (!option && !value) return true;
    if (!option || !value) return false;
    return option.codigo === value.codigo;
  };
}
