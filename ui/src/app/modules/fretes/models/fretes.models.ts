export class Frete {
  constructor(
    public cidade: string,
    public valor_frete: number,
    public prazo_entrega_dias: number,
    public id?: number
  ) {}

  static fromJson(json: any): Frete {
    return new Frete(
      json.cidade,
      json.valor_frete,
      json.prazo_entrega_dias,
      json.id
    );
  }

  toJson(): any {
    return {
      cidade: this.cidade,
      valor_frete: this.valor_frete,
      prazo_entrega_dias: this.prazo_entrega_dias,
      id: this.id
    };
  }
}
