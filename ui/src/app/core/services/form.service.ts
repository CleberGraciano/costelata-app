import { ElementRef, Injectable } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidationErrors} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private formBuilder: FormBuilder) { }

  validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.updateValueAndValidity({ emitEvent: true, onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      }
    });
  }

  getValidationsMessages(form: FormGroup, formElement: ElementRef<any>): string[] {
    const validationMessageMessages: string[] = [];

    Object.keys(form.controls).forEach(key => {
      const controlErrors: ValidationErrors | null | undefined = form?.get(key)?.errors;
      if (controlErrors != null) {

        const labelElements = formElement.nativeElement.querySelectorAll(`label[for='${key}']`);
        if (labelElements.length) {
          validationMessageMessages.push(`${labelElements[0].textContent}`);
        }

        // Object.keys(controlErrors).forEach(keyError => {
        //   console.log('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
        // });
      }
    });
    return validationMessageMessages;
  }

  createValidationList(validationMessageMessages:string[]) {
    let message = '';
    validationMessageMessages.forEach(x=> message+=`<li>${x}</li>`);
    return `<br><ul class="validation-messages">${message}</ul>`;
  }
}
