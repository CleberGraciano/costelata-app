import { Component, AfterViewInit, inject } from "@angular/core";
import { FormArray, Validators, FormBuilder } from "@angular/forms";
import { UnidadesService } from "@core/services/unidade.service";
import { CategoriasService } from "@modules/categorias/services/categorias.service";
import { Cliente } from "@modules/clientes/models/clientes.models";
import { Produto } from "@modules/produtos/models/produtos.model";
import { ProdutosService } from "@modules/produtos/services/produtos.service";
import { BaseFormComponent } from "@shared/components/classes-base/base-form.component";
import { takeUntil } from "rxjs";

@Component({
  selector: 'app-produtos-form',
  templateUrl: './produtos-form.component.html',
  styleUrls: ['./produtos-form.component.css']
})
export class ProdutosFormComponent extends BaseFormComponent<Produto> implements AfterViewInit {
  override fb = inject(FormBuilder);

  private service = inject(ProdutosService);
  private categoriasService = inject(CategoriasService);
  private unidadesService = inject(UnidadesService);

  categorias: any;
  unidades: any;

  // imagens: { file: File | null; preview: string }[] = [];
  imagemModalVisivel = false;
  imagemSelecionada: string | null = null;

  override buildForm(): void {
    this.form = this.fb.group({
      codigo: ['', Validators.required],
      descricao: ['', [Validators.required, Validators.minLength(3)]],
      descr_detalhada: [''],
      descricao_familia: [undefined, Validators.required],
      codigo_familia: [undefined, Validators.required],
      unidade: [undefined, Validators.required],
      valorUnitario: [0, [Validators.required, Validators.min(0.01)]],
      categoria: [undefined, Validators.required],
      aliquota_icms: [0, [Validators.min(0)]],
      cst_icms: [''],
      csosn_icms: [''],
      aliquota_pis: [0, [Validators.min(0)]],
      cst_pis: [''],
      aliquota_cofins: [0, [Validators.min(0)]],
      cst_cofins: [''],
      red_base_icms: [0, [Validators.min(0)]],
      red_base_pis: [0, [Validators.min(0)]],
      red_base_cofins: [0, [Validators.min(0)]],
      per_icms_fcp: [0, [Validators.min(0)]],
      motivo_deson_icms: [''],
      codigo_beneficio: [''],
      ncm: [''],
      cfop: [''],
      cest: [''],
      dias_garantia: [0, [Validators.min(0)]],
      estoque_minimo: [0, [Validators.min(0)]],
      quantidade_estoque: [0, [Validators.min(0)]],
      profundidade: [0, [Validators.min(0)]],
      largura: [0, [Validators.min(0)]],
      peso_bruto: [0, [Validators.min(0)]],
      peso_liq: [0, [Validators.min(0)]],
      armazenamento: [''],
      rendimento_por_produto: [''],
      argumentos_venda: [''],
      exposicao_pdv: [''],
      explicacoes_etiqueta: [''],
      informacoes_adicionais: [''],
      imagens: this.fb.array([
        this.fb.control('')
      ]),
      videos: this.fb.array([
        this.fb.control('')
      ]),
      outrosArquivos: this.fb.array([
        this.fb.control('')
      ])
    });
    this.carregarSelects();
  }

  carregarSelects(): void {
    this.categoriasService.combo().subscribe((dados) => {
      this.categorias = dados;
    });

    this.unidadesService.combo().subscribe((dados) => {
      this.unidades = dados;
    });
  }

  override buscarDados(id: number): void {
    this.activatedRoute.queryParams.pipe(takeUntil(this.destroy$)).subscribe((queryParams) => {
      if (queryParams) {
        console.log(queryParams);
        this.service.itemById(queryParams).subscribe((produto: Produto) => {
          console.log(produto);
          this.form.patchValue(produto);
        });
      }
    });
  }

  ngAfterViewInit(): void {
    this.setHeader('Produtos', 'produtos');
  }

  salvarProduto() {
    const sucesso = this.salvar(this.form.getRawValue() as Produto);
    if (sucesso) {
      const produto: Produto = this.form.getRawValue() as Produto;
      console.log('Produto salvo:', produto);
    }
  }

  onUpload(event: any): void {
    const file: File = event.files[0];
    const reader = new FileReader();

    // reader.onload = () => {
    //   this.imagens.push({
    //     file,
    //     preview: reader.result as string
    //   });
    //   // Limpa o input do upload
    //   if (event && event.originalEvent && event.originalEvent.target) {
    //     event.originalEvent.target.value = '';
    //   }
    //   // Se usar referência do template, pode usar: this.fileUpload.clear();
    // };

    reader.readAsDataURL(file);
  }

  visualizarImagem(preview: string): void {
    this.imagemSelecionada = preview;
    this.imagemModalVisivel = true;
  }

  removerImagem(index: number): void {
    // this.imagens.splice(index, 1);
  }

  get imagens(): FormArray {
    return this.form.get('imagens') as FormArray;
  }

  get videos(): FormArray {
    return this.form.get('videos') as FormArray;
  }

  get outrosArquivos(): FormArray {
    return this.form.get('outrosArquivos') as FormArray;
  }
}
