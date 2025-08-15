import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'objectValue',
  standalone: true,
})
export class ObjectValuePipe implements PipeTransform {
  transform(value: any): any {
    const objectMaped = Object.entries(value).map((item) => {
      return { id: item[0], ocorrencia: item[1] };
    });
    return objectMaped;
  }
}
