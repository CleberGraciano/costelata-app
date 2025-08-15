import { Directive, Self, Inject, Optional, Host, ComponentRef, ViewContainerRef, ComponentFactoryResolver, Input } from '@angular/core';
import { NgControl } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { FormSubmitDirective } from '@shared/components/dynamic-form/directives/form-submit/form-submit.directive';
import { Observable, EMPTY, merge, fromEvent } from 'rxjs';
import { ControlErrorComponent } from '@shared/components/dynamic-form/components/control-error/control-error.component';
import { ControlErrorContainerDirective } from './control-error-container.directive';
import { FORM_ERRORS } from '@core/config/form-errors.config';

@UntilDestroy()
@Directive({
  selector: '[formControl], [formControlName]'
})
export class ControlErrorDirective {
  submit$: Observable<Event> | any;
  ref!: ComponentRef<ControlErrorComponent>;
  container: ViewContainerRef;
  touched$ = fromEvent(this.element, 'blur')
  @Input() noKeyPressError!: boolean;

  constructor(@Self() private control: NgControl,
    @Optional() @Host() private form: FormSubmitDirective,
    @Inject(FORM_ERRORS) private errors:any,
    private viewContainerRef: ViewContainerRef,
    private resolver: ComponentFactoryResolver,
    @Optional() private controlErrorContainer: ControlErrorContainerDirective) {

    this.container = controlErrorContainer ? controlErrorContainer.viewContainerRef : viewContainerRef;
    this.submit$ = this.form ? this.form.submit$ : EMPTY;

  }

  ngOnInit() {

    let merged;

    if (!this.noKeyPressError)
      merged = merge(
        this.submit$,
        <any>this.control.valueChanges,
        <any>this.touched$,
        <any>this.control.statusChanges,
      );
    else
      merged = merge(
        this.submit$,
        this.touched$,
      );

    merged.pipe(
      untilDestroyed(this)
    ).subscribe(() => {
      const controlErrors = this.control.errors;
      if (controlErrors) {
        const firstKey = Object.keys(controlErrors)[0];

        const getError = this.errors[firstKey];
        if (getError) {
          const text = getError(controlErrors[firstKey]);
          this.setError(text);
        }
      } else if (this.ref) {
        this.setError(<any>null);
      }
    })
  }

  setError(text: string): void {

    if (!this.ref) {
      const factory = this.resolver.resolveComponentFactory(ControlErrorComponent);
      this.ref = this.container.createComponent(factory);
    }
    this.ref.instance.text = text;
    if (text)
      this.element.classList.add("is-invalid");
    else
      this.element.classList.remove("is-invalid");
  }

  ngOnDestroy() {

  }

  get element() {
    return this.viewContainerRef.element.nativeElement;
  }
}
