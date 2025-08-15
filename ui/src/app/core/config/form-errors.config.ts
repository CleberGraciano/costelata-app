import { InjectionToken } from '@angular/core';


export const defaultErrors = {
    required: (error:any) => `Campo obrigatório`,
    minlength: ({ requiredLength, actualLength }:any) => `Informe ao menos ${requiredLength} caracteres.`,
    maxlength: ({ requiredLength, actualLength }:any) => `O máximo de caracteres é de ${requiredLength}.`,
    email: (error:any) => `Formato de email inválido.`,
    cpf: (error:any) => `Formato de CPF inválido.`,
    telefone: (error:any) => `Formato de telefone inválido.`,
    cnpj: (error:any) => `Formato de CNPJ inválido.`,
    nullValue: (error:any) => `Campo Obrigatório.`,
    ngbDate: (error:any) => `${error.requiredAfter ? 'A data deve ser menor que ' + error.requiredAfter.day + '/' + error.requiredAfter.month + '/' + error.requiredAfter.year : error.requiredBefore ? 'A data deve ser maior que ' + error.requiredAfter.day + '/' + error.requiredAfter.month + '/' + error.requiredAfter.year : 'Data Inválida'}.`,
    contrato: (error:any) => `Formato de contrato inválido.`,
    min: (error:any) => `O valor mínimo é ${error.min}.`,
    max: (error:any) => `O valor máximo é ${error.max}`,
    pattern: (error:any) => `Formato inválido.`,
    fileType: (error:any) => `O Tipo de arquivo ${error} não é suportado.`,
    imageSize: (error:any) => `A imagem precisa ter o tamanho menor ou igual a ${error.width}x${error.height}.`,
    host: (error:any) => `Endereço inválido.`,
    whitespace: (error:any) => 'Espaços não são permitidos.',
    currency: (error:any) => `Valor inválido.`,
    cep: (error:any) => `Formato de CEP inválido.`,
    data: (error:any) => `Formato de data inválido.`,
    arrayRequired:(error:any)=>`Campo Obrigatório.`,
    rg: (error:any) => `Formato de RG inválido.`,
}

export const FORM_ERRORS = new InjectionToken('FORM_ERRORS', {
    providedIn: 'root',
    factory: () => defaultErrors
})
