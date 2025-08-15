import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatDoisDigitosDecimal',
  standalone: true
})
export class FormatDoisDigitosDecimalPipe implements PipeTransform {
  transform(value: any) {
    if (value === null || value === undefined || isNaN(value)) {
      return '';
    }

    const valorNumerico = parseFloat(value);

    if (isNaN(valorNumerico)) {
      return '';
    }

    const valorFormatado = valorNumerico / 100;
    const valorFinal = valorFormatado.toFixed(2);

    return valorFinal;
  }
}
