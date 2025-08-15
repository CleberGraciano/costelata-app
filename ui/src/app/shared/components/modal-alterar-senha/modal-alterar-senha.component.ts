import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-modal-alterar-senha',
  templateUrl: './modal-alterar-senha.component.html',
  styleUrls: ['./modal-alterar-senha.component.css']
})
export class ModalAlterarSenhaComponent {
  showSenhaProvisoria = false;
  showNovaSenha = false;
  showConfirmarSenha = false;
  @Input() senhaProvisoria: string = '';
  @Input() display: boolean = false;
  @Output() fechar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<{ senhaProvisoria: string, novaSenha: string }>();

  form: FormGroup;
  erro: string = '';

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      senhaProvisoria: ['', Validators.required],
      novaSenha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    });
  }

  ngOnChanges() {
    this.form.patchValue({ senhaProvisoria: this.senhaProvisoria });
  }

  onSalvar() {
    if (this.form.invalid) {
      this.erro = 'Preencha todos os campos corretamente.';
      return;
    }
    const { novaSenha, confirmarSenha } = this.form.value;
    if (novaSenha !== confirmarSenha) {
      this.erro = 'As senhas não coincidem.';
      return;
    }
    this.erro = '';
    this.salvar.emit({ senhaProvisoria: this.form.value.senhaProvisoria, novaSenha });
  }

  onFechar() {
    this.fechar.emit();
  }
}
