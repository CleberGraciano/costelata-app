import { Pipe, PipeTransform } from '@angular/core';
import { dayNMonthFormat } from '../classes/utils';

@Pipe({
  name: 'dataHoraSec',
  standalone: true,
})
export class DataHoraSecPipe implements PipeTransform {
  transform(value: string): string {
    if (Boolean(value) && Boolean(value !== '')) {
      const date = new Date(value);
      return `${dayNMonthFormat(date.getDate())}-${dayNMonthFormat(
        date.getMonth() + 1
      )}-${date.getFullYear()} ${dayNMonthFormat(
        date.getHours()
      )}:${dayNMonthFormat(date.getMinutes())}:${dayNMonthFormat(
        date.getSeconds()
      )}`;
    }

    return '';
  }
}
