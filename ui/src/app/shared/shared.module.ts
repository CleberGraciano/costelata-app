import { CommonModule } from "@angular/common";
import { LOCALE_ID, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { PrimeNgModule } from "./primeng.module";
import { BlockCopyPasteCutDirective } from "./directives/block-copy-past-cut.directive";
import { ModalAlterarSenhaComponent } from './components/modal-alterar-senha/modal-alterar-senha.component';
import { DynamicFormModule } from "./components/dynamic-form/dynamic-form.module";
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from "ngx-mask";
import { ConfirmationService, MessageService } from "primeng/api";
import { CPFPipe } from "./pipes/cpf.pipe";
import { NgxPermissionsModule } from "ngx-permissions";
import { SituacaoEventoPipe } from "./pipes/situacao-evento.pipe";
import { TelefonePipe } from "./pipes/telefone.pipe";

@NgModule({
  declarations: [
    BlockCopyPasteCutDirective,
    CPFPipe,
    SituacaoEventoPipe,
    TelefonePipe,
    ModalAlterarSenhaComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PrimeNgModule,
    DynamicFormModule,
    NgxMaskDirective,
    NgxMaskPipe,
    NgxPermissionsModule.forChild(),
  ],
  exports:[
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BlockCopyPasteCutDirective,
    PrimeNgModule,
    DynamicFormModule,
    NgxMaskDirective,
    NgxMaskPipe,
    CPFPipe,
    SituacaoEventoPipe,
    TelefonePipe,
    ModalAlterarSenhaComponent
  ],
  providers:[
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    MessageService,
    ConfirmationService,
    provideNgxMask()
  ]
})
export class SharedModule { }
