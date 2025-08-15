import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { BaseListComponent } from '@shared/components/classes-base/base-list.component';
import { CategoriasService } from '@modules/categorias/services/categorias.service';
import { FormGroup } from '@angular/forms';
import { StatusService } from '@core/services/status.service';

@Component({
  selector: 'app-categorias-list',
  templateUrl: './categorias-list.component.html',
  styleUrls: ['./categorias-list.component.css']
})
export class CategoriasListComponent extends BaseListComponent<any> implements OnInit, AfterViewInit {

  filtroForm!: FormGroup;
  categorias: any[] = [];
  override totalRecords = 0;
  carregando = false;
  
  service = inject(CategoriasService);
  override status = inject(StatusService).listarStatus();
  
  ngOnInit(): void {
    this.inicializarFormulario();
    this.pesquisar();
  }

  ngAfterViewInit(): void {
    this.setHeader('Categorias', 'categorias');
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
        this.categorias = dados.content || dados;
        this.totalRecords = dados.totalElements ?? dados.totalRecords ?? this.categorias.length ?? 0;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao buscar categorias!' });
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

  editar(categoria: any): void {
    this.router.navigate(['/categorias/editar', categoria.id]);
  }

  alterarStatus(categoria: any): void {
    const message = `Deseja realmente ${categoria.status === 'Ativo' ? 'bloquear' : 'ativar'} a categoria <strong>${categoria.descricao}</strong>?`;
    this.dialogServiceApp.confirm(message, () => {
      this.service.alterarStatus(categoria.id, categoria.status === 'Ativo' ? 'Bloqueado' : 'Ativo').subscribe({
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
