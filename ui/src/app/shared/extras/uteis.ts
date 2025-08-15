import { AbstractControl, FormGroup, ValidatorFn } from "@angular/forms";
import { MessageService } from "primeng/api";


export function convertedTranslation() {
  const translatedMonths = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho',
    'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];

  const translatedDays = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'];

  return {
    apply: 'Aplicar',
    clear: 'Limpar',
    accept: 'Sim',
    reject: 'Não',
    firstDayOfWeek: 0,
    dayNames: translatedDays,
    dayNamesShort: translatedDays.map(day => day.substring(0, 3)),
    dayNamesMin: translatedDays.map(day => day.substring(0, 2)),
    monthNames: translatedMonths,
    monthNamesShort: translatedMonths.map(month => month.substring(0, 3)),
    today: 'Hoje'
  };
}

export function removeDuplicates(array: any[], prop: string): any[] {
  return array.filter((obj, index, self) =>
      index === self.findIndex((item) =>
        item[prop] === obj[prop]
      )
  );
}

export function generateQueryParamList(object: any, keys: Array<string>) {
  let params = {};
  keys.forEach((keyName) => {
    if (object[keyName]) {
      Object.assign(params, {[keyName]: object[keyName]});
    }
  });
  return params;
}

export function generateQueryParamSingle(keyName: string, value: any) {
  let params = {};
  Object.assign(params, {[keyName]: value});
  return params;
}

export function cleanEmptyValues(objectToFindValue: any) {
  return Object.keys(objectToFindValue).filter(
    (obj) => objectToFindValue[obj] !== ''
  );
}


export function aplicarFiltrosUrl(filter: any) {
  const parametrosURL = new URLSearchParams();
  for (const key in filter) {
    if (filter.hasOwnProperty(key) && filter[key]) {
      parametrosURL.set(key, filter[key]);
    }
  }

  const urlAtual = window.location.origin + window.location.pathname;
  const novaURL = `${urlAtual}?${parametrosURL.toString()}`;

  history.pushState({}, '', novaURL);
  return
}

export function inicializarFiltrosComBaseNaURL(form: FormGroup) {
  const parametrosURL = new URLSearchParams(window.location.search);

  const parametros: { [key: string]: any } = {};

  parametrosURL.forEach((valor, chave) => {
    parametros[chave] = valor;
  });

  applyDateFilters(parametros);

  if(parametros['dataInicio']) {
    parametros['dataInicio'] = new Date(parametros['dataInicio']);
  }

  if(parametros['dataTermino']) {
    parametros['dataTermino'] = new Date(parametros['dataTermino']);
  }

  form.patchValue(parametros);
  return true
}

function converterDataParametro(parametro: string): string | null {
  return parametro ? new Date(parametro).toString() : null;
}

function applyDateFilters(parametros: any) {
  parametros['dataInicio'] = converterDataParametro(parametros['dataInicio']);
  parametros['dataTermino'] = converterDataParametro(parametros['dataTermino']);
}

export function alterarUrlSemRecarregar(parametrosURL: string) {
  const novaURL = `${parametrosURL.toString()}`;
  history.pushState({}, '', novaURL);
}

export function dataInicioMenorQueTerminoValidator(dataS: string, dataF: string, messageService: MessageService): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const dataInicio = control.get(dataS)?.value;
    const dataTermino = control.get(dataF)?.value;

    if (dataInicio && dataTermino && dataInicio > dataTermino) {
      messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A data de início não pode ser maior que a data de término.' });
      return { dataInicioMaiorQueTermino: true };
    }
    return null;
  };
}

export function cepValidator(messageService: MessageService): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let cep = control.value;
    let numbers = '';

      if (cep){
        cep = cep.replace(/\s+/g, '');
        cep = cep.replace(/\D/g, '');
        numbers = cep.match(/\d/g);
    }

    if (cep != undefined && cep != null && cep != '' && numbers.length != 8) {
        return {
            cep: true
        }
    }
    return null;
  };
}


export function telefoneValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const value = control.value?.toString() || '';
    const cleaned = value.replace(/\D/g, '');

    if (cleaned.length !== 10 && cleaned.length !== 11) {
      return { telefone: true };
    }

    return null;
  };
}
