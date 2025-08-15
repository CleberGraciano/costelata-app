import { Component, AfterViewInit } from '@angular/core';
import { Validators, FormArray } from '@angular/forms';
import { Pedido } from '../../models/pedidos.model';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';

@Component({
  selector: 'app-pedidos-form',
  templateUrl: './pedidos-form.component.html',
  styleUrls: ['./pedidos-form.component.css']
})
export class PedidosFormComponent extends BaseFormComponent<Pedido> implements AfterViewInit {

  protected buildForm(): void {
    this.form = this.fb.group({
      numeroPedido: ['', [Validators.required]],
      dataEmissao: ['', [Validators.required]],
      cliente: ['', [Validators.required]],
      cnpjCpf: ['', [Validators.required]],
      telefone: [''],
      email: ['', [Validators.required, Validators.email]],
      valorTotal: [0, [Validators.required, Validators.min(0)]],
      status: ['', [Validators.required]],
      endereco: [''],
      itens: this.fb.array([])
    });
  }

  protected buscarDados(id: number): void {
    // Implementar busca de dados do pedido por id se necessário
  }

  ngAfterViewInit(): void {
    this.setHeader('Pedidos', 'pedidos');
  }

  get itens(): FormArray {
    return this.form.get('itens') as FormArray;
  }

  adicionarItem() {
    (this.itens as any).push(this.fb.group({
      produto: ['', [Validators.required]],
      quantidade: [1, [Validators.required, Validators.min(1)]],
      valorUnitario: [0, [Validators.required, Validators.min(0.01)]]
    }));
  }

  removerItem(index: number) {
    (this.itens as any).removeAt(index);
  }

  override salvar(objEnvio: Pedido): boolean {
    if (this.form.valid) {
      // Implementar lógica de salvar pedido
      // Exemplo: this.pedidosService.criar(objEnvio).subscribe();
      return true;
    }
    return false;
  }
}
