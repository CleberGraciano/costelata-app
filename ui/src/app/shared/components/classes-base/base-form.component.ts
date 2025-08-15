// core/base/base-form.component.ts
import { Directive, ElementRef, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { RouteAction } from '@core/enum/actions.enum';
import { FormService } from '@core/services/form.service';
import { MessageService } from 'primeng/api';
import { HeaderSignalService } from 'src/app/header-signal.service';
import { ClientesService } from '@modules/clientes/services/clientes.service';
import { Cliente } from '@modules/clientes/models/clientes.models';
import { LocalStorageService } from '@core/local-storage/local-storage.service';

@Directive()
export abstract class BaseFormComponent<T> implements OnInit, OnDestroy {
   @ViewChild('formElement') formElement!: ElementRef;

  form!: FormGroup;
  idParams!: number;
  acao!: RouteAction;
  criar!: boolean;
  editar!: boolean;
  visualizar!: boolean;

  isMinhaConta!: boolean;
  mensagemPrazoCompra!: string;

  cepMask = '00000-000';
  cnpjMask = '00.000.000/0000-00';
  cnpjCpfMask = '000.000.000-00||00.000.000/0000-00';
  telefoneMask = '(00) 0000-0000||(00) 00000-0000';

  perfis: { codigo: string; descricao: string; }[] | undefined;
  status: { codigo: string; descricao: string; }[] | undefined;
  estados: { codigo: string; descricao: string; }[] | undefined;
  tipoServicos: { codigo: string; descricao: string; }[] | undefined;
  cidades: any[] = [];

  codigoCliente: any;

  protected destroy$ = new Subject<void>();

  constructor(
    protected fb: FormBuilder,
    protected router: Router,
    protected activatedRoute: ActivatedRoute,
    protected formService: FormService,
    protected messageService: MessageService,
    protected headerSignal: HeaderSignalService,
    protected localStorageService: LocalStorageService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.setParametros();
  }

  protected setHeader(titulo: string, rotaBase: string): void {
    this.headerSignal.title.set(titulo);
    this.headerSignal.actionPage.set(this.acao);
    this.headerSignal.breadcrumbs.set([
      { label: 'Início', url: '/' },
      { label: titulo, url: `/${rotaBase}` },
      { label: `${this.acao ?? ''} ${titulo.slice(0, -1)}`, url: `/${rotaBase}/${this.acao}` }
    ]);
    this.headerSignal.actionButton.set(null);
  }

  protected abstract buildForm(): void;
  protected abstract buscarDados(id?: any): void;

  protected setParametros(): void {
    const rotaBase = this.activatedRoute.snapshot.pathFromRoot.map(route => route.routeConfig?.path).filter(Boolean)[0];
    if(rotaBase === 'minha-conta') {
      this.isMinhaConta = true;
      const usuarioLogado = this.localStorageService.getItem('usuario');
      this.idParams = usuarioLogado?.id;
      this.buscarDados(this.idParams);
    }

    this.activatedRoute.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      this.idParams = params['id'];
      if (this.idParams) {
        this.buscarDados(this.idParams);
      }
    });

    this.activatedRoute.queryParams.pipe(takeUntil(this.destroy$)).subscribe((queryParams) => {
      if(!this.idParams && queryParams) {
        this.codigoCliente = queryParams;
        console.log('codigoCliente', this.codigoCliente);
        this.buscarDados();
      }
    });

    this.activatedRoute.data.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      this.acao = params['action'];
      this.criar = this.acao === RouteAction.Cadastrar;
      this.editar = this.acao === RouteAction.Editar;
      this.visualizar = this.acao === RouteAction.Visualizar;
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
}
