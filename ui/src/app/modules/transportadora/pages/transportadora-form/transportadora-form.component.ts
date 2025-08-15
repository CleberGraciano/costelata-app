import { Component, AfterViewInit, inject } from '@angular/core';
import { Validators } from '@angular/forms';
import { Transportadora } from '../../models/transportadora.model';
import { TransportadoraService } from '../../services/transportadora.service';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';

@Component({
  selector: 'app-transportadora-form',
  templateUrl: './transportadora-form.component.html',
  styleUrls: ['./transportadora-form.component.css']
})
export class TransportadoraFormComponent extends BaseFormComponent<Transportadora> implements AfterViewInit {
  
  private service = inject(TransportadoraService);

  ngAfterViewInit(): void {
    this.setHeader('Transportadoras', 'transportadora');
  }

  override buildForm(): void {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      cnpj: ['', Validators.required],
      inscricaoEstadual: [''],
      telefone: ['', Validators.required]
    });
  }

  override buscarDados(id: number): void {
    this.service.itemById(id).subscribe((transportadora) => {
      if (transportadora) {
        this.form.patchValue(transportadora);
      }
    });
  }

  salvarTransportadora(): void {
    const transportadora: any = this.form.getRawValue();
    const formValid = this.salvar(transportadora);

    if (formValid) {
      if (this.idParams)
        this.callBackEditar(transportadora)
      else
        this.callBackCriar(transportadora);
    }
  }

  callBackCriar(transportadora: Transportadora): void {
    this.service.insert(transportadora).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Transportadora criada com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar transportadora.' });
        // console.error('Error creating transportadora:', err);
      }
    });
  }

  callBackEditar(transportadora: Transportadora): void {
    this.service.edit(this.idParams, transportadora).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Transportadora editada com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao editar transportadora.' });
        // console.error('Error editing transportadora:', err);
      }
    });
  }

  irParaLista(): void {
    this.router.navigate(['/transportadoras']);
  }
}
