import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TransportadoraService } from '../../services/transportadora.service';
import { Transportadora } from '../../models/transportadora.model';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import { StatusService } from '@core/services/status.service';

@Component({
  selector: 'app-transportadora-list',
  templateUrl: './transportadora-list.component.html'
})
export class TransportadoraListComponent extends BaseListComponent<any> implements OnInit, AfterViewInit {
  
  filtroForm!: FormGroup;
  transportadoras: Transportadora[] = [];
  carregando = false;

  service = inject(TransportadoraService);
  override status = inject(StatusService).listarStatus();

  ngOnInit(): void {
    this.inicializarFormulario();
    this.pesquisar();
  }

  ngAfterViewInit(): void {
    this.setHeader('Transportadoras', 'transportadoras');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      nome: [''],
      cnpj: [''],
      status: [null]
    });
  }

  pesquisar(): void {
    this.carregando = true;
    const filtro = this.filtroForm.getRawValue();

    filtro.status = filtro.status?.codigo ?? null;

    this.service.filter(filtro).subscribe({
      next: (dados: any) => {
        this.transportadoras = dados.content || dados;
        this.totalRecords = dados.totalElements ?? dados.totalRecords ?? this.transportadoras.length ?? 0;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar transportadoras!' });
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

  editar(transportadora: Transportadora): void {
    this.router.navigate(['/transportadoras/editar', transportadora.id]);
  }

  alterarStatus(transportadora: Transportadora): void {
    const message = `Deseja realmente ${transportadora.status === 'Ativo' ? 'bloquear' : 'ativar'} a transportadora <strong>${transportadora.nome}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.service.alterarStatus(transportadora.id, transportadora.status === 'Ativo' ? 'Bloqueado' : 'Ativo').subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Status alterado com sucesso!' });
          this.pesquisar();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao alterar status!' });
        }
      });
    });
  }
}
