import { AbstractControl, ValidationErrors } from '@angular/forms';

export function emailCustomValidator(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  // Regex RFC 5322 simplificado
  const emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
  return emailRegex.test(control.value) ? null : { emailInvalid: true };
}
