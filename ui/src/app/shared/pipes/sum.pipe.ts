import { Pipe, PipeTransform } from '@angular/core';
import {sum} from "../classes/utils";

@Pipe({
  name: 'sum',
  standalone: true,
  pure: false,
})
export class SumPipe implements PipeTransform {
  transform(
    values: any,
    isFormArray: boolean = false,
    inputs: string[] = [],
    monetary: boolean = true
  ): any {
    if (isFormArray) {
      let valuesNumber = [];
      values.forEach((element) => {
        inputs.forEach((sumKey) => {
          if (element.value[sumKey]) {
            valuesNumber.push(element.value[sumKey]);
          } else {
            valuesNumber.push(0);
          }
        });
      });

      return sum(valuesNumber, monetary);
    } else {
      let valuesNumber = [];
      inputs.forEach((input) => {
        if (values[input]) {
          valuesNumber.push(values[input]);
        } else {
          valuesNumber.push(0);
        }
      });

      return sum(valuesNumber, monetary);
    }
  }
}
