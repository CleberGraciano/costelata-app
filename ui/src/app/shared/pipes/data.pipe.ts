import { Pipe, PipeTransform } from '@angular/core';
import { dayNMonthFormat } from '../classes/utils';

@Pipe({
  name: 'data',
  standalone: true,
})
export class DataPipe implements PipeTransform {
  transform(value: string): string {
    if (Boolean(value) && Boolean(value !== '')) {
      const date = new Date(value.substring(0, 10).replace(/\-/g, '/'));
      return `${dayNMonthFormat(date.getDate())}/${dayNMonthFormat(
        date.getMonth() + 1
      )}/${date.getFullYear()}`;
    }

    return '';
  }
}
