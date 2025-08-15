import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { StatusService } from '@core/services/status.service';
import { FretesService } from '@modules/fretes/services/fretes.service';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';

@Component({
  selector: 'app-fretes-list',
  templateUrl: './fretes-list.component.html',
  styleUrls: ['./fretes-list.component.css']
})
export class FretesListComponent extends BaseListComponent<any> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  fretes: any[] = [];
  override totalRecords = 0;
  carregando = false;
  
  service = inject(FretesService);
  override status = inject(StatusService).listarStatus();
  
  ngOnInit(): void {
    this.inicializarFormulario();
    this.pesquisar();
  }

  ngAfterViewInit(): void {
    this.setHeader('Fretes', 'fretes');
  }

  inicializarFormulario(): void {
    this.filtroForm = this.fb.group({
      nome: [''],
      status: [undefined]
    });
  }

  pesquisar(): void {
    this.carregando = true;
    const filtro = this.filtroForm.getRawValue();

    filtro.status = filtro.status?.codigo ?? null;

    this.service.filter(filtro).subscribe({
      next: (dados: any) => {
        this.fretes = dados.content || dados;
        this.totalRecords = dados.totalElements ?? dados.totalRecords ?? this.fretes.length ?? 0;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar fretes!' });
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

  editar(frete: any): void {
    this.router.navigate(['/fretes/editar', frete.id]);
  }

  alterarStatus(frete: any): void {
    const message = `Deseja realmente ${frete.status === 'Ativo' ? 'bloquear' : 'ativar'} o frete <strong>${frete.descricao}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.service.alterarStatus(frete.id, frete.status === 'Ativo' ? 'Bloqueado' : 'Ativo').subscribe({
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
