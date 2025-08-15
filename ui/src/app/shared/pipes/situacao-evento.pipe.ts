import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'situacaoEvento',
})
export class SituacaoEventoPipe implements PipeTransform {
  transform(value: string): string {

    switch (value) {
      case SituacaoEventoEnum.Inscrito:
        return SituacaoEventoStringEnum.Inscrito;

      case SituacaoEventoEnum.NaoInscrito:
        return SituacaoEventoStringEnum.NaoInscrito;

      case SituacaoEventoEnum.NaoRealizado:
        return SituacaoEventoStringEnum.NaoRealizado;

      case SituacaoEventoEnum.Cancelado:
        return SituacaoEventoStringEnum.Cancelado;

      case SituacaoEventoEnum.Realizado:
      default:
        return 'Realizado';
    }

  }
}

export enum SituacaoEventoEnum {
  Inscrito = 'INSCRITO',
  NaoInscrito = 'NAO_INSCRITO',
  Realizado = 'REALIZADO',
  NaoRealizado = 'NAO_REALIZADO',
  Cancelado = 'CANCELADO',
}

export enum SituacaoEventoStringEnum {
  Inscrito = 'Inscrito',
  NaoInscrito = 'Não Inscrito',
  Realizado = 'Realizado',
  NaoRealizado = 'Não Realizado',
  Cancelado = 'Cancelado'
}
