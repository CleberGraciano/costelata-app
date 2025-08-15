import { Pipe, PipeTransform } from '@angular/core';
import {TipoControle} from "@core/enum/tipo-controle.enum";

@Pipe({
  name: 'situation',
  standalone: true,
})
export class SituationPipe implements PipeTransform {
  transform(key: string): string {
    if (key === 'REGISTRO_PRESENCA') {
      return TipoControle.REGISTRO_PRESENCA
    } else if (key === 'CONTROLE_ACESSO') {
      return TipoControle.CONTROLE_ACESSO
    }

    return '-';
  }
}
