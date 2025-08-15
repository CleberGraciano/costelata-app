import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { ControlErrorDirective } from './directives/control-error/control-error.directive';
import { ControlErrorComponent } from './components/control-error/control-error.component';
import { ControlErrorContainerDirective } from './directives/control-error/control-error-container.directive';
import { FormSubmitDirective } from './directives/form-submit/form-submit.directive';


@NgModule({
  declarations: [
    FormSubmitDirective,
    ControlErrorDirective,
    ControlErrorComponent,
    ControlErrorContainerDirective
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule
  ],
  exports: [
    FormSubmitDirective,
    ControlErrorDirective,
    ControlErrorComponent,
    ControlErrorContainerDirective
  ]
})
export class DynamicFormModule { }
