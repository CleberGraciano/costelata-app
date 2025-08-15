import { Pipe, PipeTransform } from '@angular/core';
import { formatCNPJ } from '../classes/utils';

@Pipe({
  name: 'cnpj',
  standalone: true,
})
export class CnpjPipe implements PipeTransform {
  transform(value: string): string {
    return formatCNPJ(value) ?? '-';
  }
}
