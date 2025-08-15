import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import { Validators } from '@angular/forms';
import { EstadosService } from '@core/services/estados.service';
import { PerfilService } from '@core/services/perfil.service';
import { StatusService } from '@core/services/status.service';
import { Frete } from '@modules/fretes/models/fretes.models';
import { FretesService } from '@modules/fretes/services/fretes.service';
import { TransportadoraService } from '@modules/transportadora/services/transportadora.service';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';

@Component({
  selector: 'app-fretes-form',
  templateUrl: './fretes-form.component.html',
  styleUrls: ['./fretes-form.component.css']
})
export class FretesFormComponent extends BaseFormComponent<any> implements AfterViewInit {

  override estados = inject(EstadosService).listarEstados();
  override status = inject(StatusService).listarStatus();
  override perfis = inject(PerfilService).listarPerfis();
  transportadoras: any[] = [];

  private service = inject(FretesService);
  private transportadoraService = inject(TransportadoraService);

  override buildForm(): void {
    this.form = this.fb.group({
      descricao: ['', Validators.required],
      estado: [undefined, Validators.required],
      cidade: [undefined, Validators.required],
      valorFrete: [0, [Validators.required, Validators.min(0)]],
      prazoEntregaDias: [0, [Validators.required, Validators.min(1)]],
      pedidoMinimo: [0, [Validators.required, Validators.min(1)]],
      transportadoras: [undefined, Validators.required]
    });

    this.carregarSelects();
  }

  override buscarDados(id: number): void {
    this.service.itemById(id).subscribe((frete: any) => {
      this.form.patchValue(frete);
    });
  }

  carregarSelects(): void {
    console.log('Transportadoras:');
    this.transportadoraService.combo().subscribe((transportadoras) => {
      this.transportadoras = transportadoras;
    });
  }

  ngAfterViewInit(): void {
    this.setHeader('Fretes', 'fretes');
  }

  salvarFrete(): void {
    const fretes: any = this.form.getRawValue();
    const formValid = this.salvar(fretes);

    if (formValid) {
      if (this.idParams)
        this.callBackEditar(fretes)
      else
        this.callBackCriar(fretes);
    }
  }

  callBackCriar(fretes: any): void {
    this.service.insert(fretes).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Frete criado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar frete.' });
        // console.error('Error creating frete:', err);
      }
    });
  }

  callBackEditar(fretes: any): void {
    this.service.edit(this.idParams, fretes).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Frete editado com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao editar frete.' });
        // console.error('Error editing frete:', err);
      }
    });
  }

  irParaLista(): void {
    this.router.navigate(['/fretes']);
  }

  get totalFreteMaisImpostos() {
    const valorFrete = Number(this.form?.get('valorFrete')?.value) || 0;
    const impostos = Number(this.form?.get('impostos')?.value) || 0;
    return valorFrete + impostos;
  }
}

