import { Directive, ViewContainerRef, ViewChild } from '@angular/core';
import { ControlErrorComponent } from '@shared/components/dynamic-form/components/control-error/control-error.component';

@Directive({
  selector: '[controlErrorContainer]'
})
export class ControlErrorContainerDirective {

  @ViewChild(ControlErrorComponent,{static:false}) child !: ControlErrorComponent;

  constructor(public viewContainerRef: ViewContainerRef) { }
}