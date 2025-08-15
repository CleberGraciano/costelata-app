import { Component, AfterViewInit, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { emailCustomValidator } from 'src/app/shared/validators/email-validator';
import { Cliente } from '../../models/clientes.models';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';
import { EstadosService } from '@core/services/estados.service';
import { CidadesService } from '@core/services/cidades.service';
import { StatusService } from '@core/services/status.service';
import { PerfilService } from '@core/services/perfil.service';
import { ClientesService } from '@modules/clientes/services/clientes.service';
import { TipoServicoService } from '@core/services/tipoServico.service';
import { condicaoPagamentoService } from '@core/services/condicaoPagamento.service';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']

})
export class ClientesFormComponent extends BaseFormComponent<Cliente> implements AfterViewInit {

  service = inject(ClientesService);
  override estados = inject(EstadosService).listarEstados();
  cidadesService = inject(CidadesService);
  override status = inject(StatusService).listarStatus();
  override perfis = inject(PerfilService).listarPerfis();
  override tipoServicos = inject(TipoServicoService).listarTipos();
  condicoesPagamento = inject(condicaoPagamentoService).listarCondicoes();

  override mensagemPrazoCompra = `Prazo para pagamento por boleto liberado: à vista.`;

  ngAfterViewInit(): void {
    (this.isMinhaConta)
      ? this.setHeader('Minha Conta', 'minha-conta')
      : this.setHeader('Clientes', 'clientes');
  }

  protected buildForm(): void {
    this.form = this.fb.group({
      nome_fantasia: ['', [Validators.required, Validators.minLength(3)]],
      razao_social: ['', [Validators.required, Validators.minLength(3)]],
      cnpj_cpf: ['', [Validators.required]],
      telefone: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email, emailCustomValidator]],
      cep: [undefined, Validators.required],
      bairro: [undefined, Validators.required],
      endereco: [undefined, Validators.required],
      numero: [undefined, Validators.required],
      complemento: [undefined],
      cidade: [undefined, Validators.required],
      estado: [undefined, Validators.required],
      condicaoDePagamento: [undefined, Validators.required],
      tipoDeServico: [undefined, Validators.required]
    });

    this.carregarCidades();
  }

  carregarCidades() {
    this.estadoControl.valueChanges.subscribe((estado: any) => {
      console.log(estado);
      if (estado) {
        this.cidadesService.listarCidades({ uf: estado.codigo }).subscribe((dados: any) => {
          this.cidades = dados;
        });
      } else {
        this.cidades = [];
      }
    });
  }

  protected buscarDados(): void {
    if(this.codigoCliente) {
      this.service.itemById(this.codigoCliente).subscribe((cliente: Cliente) => {
        console.log(cliente);
        this.mensagemPrazoCompra = `Prazo para pagamento por boleto liberado: ${cliente.condicaoDePagamento}.`;
        this.form.patchValue(cliente);
      });
    }
  }

  salvarUsuario(): void {
    const cliente: Cliente = this.form.getRawValue();
    const formValid = this.salvar(cliente);

    if (formValid) {
      (this.codigoCliente)
        ? this.callBackEditar(cliente)
        : this.callBackCriar(cliente);
    }
  }

  callBackCriar(cliente: Cliente): void {
    this.service.insert(cliente).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente criado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar cliente.' });
        // console.error('Error creating client:', err);
      }
    });
  }

  callBackEditar(cliente: Cliente): void {
    cliente.codigo_cliente_integracao = this.codigoCliente.codigo_cliente_integracao ?? null;
    cliente.codigo_cliente_omie = this.codigoCliente.codigo_cliente_omie ?? null;

    this.service.edit(cliente).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente editado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao editar cliente.' });
        // console.error('Error editing client:', err);
      }
    });
  }

  irParaLista(): void {
    this.router.navigate(['/clientes']);
  }

  get estadoControl() {
    return this.form.get('estado') as FormControl<string>;
  }
}
