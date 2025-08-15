import {
  AfterViewInit,
  Component,
  inject
} from '@angular/core';
import { Validators } from '@angular/forms';
import { Categoria } from '@modules/categorias/models/categorias.model';
import { CategoriasService } from '@modules/categorias/services/categorias.service';
import { BaseFormComponent } from '@shared/components/classes-base/base-form.component';

@Component({
  selector: 'app-categorias-form',
  templateUrl: './categorias-form.component.html',
  styleUrls: ['./categorias-form.component.css']
})
export class CategoriasFormComponent extends BaseFormComponent<any> implements AfterViewInit {

  private service = inject(CategoriasService);

  imagens: { file: File | null; preview: string }[] = [];
  imagemModalVisivel = false;
  imagemSelecionada: string | null = null;

  override buildForm(): void {
    this.form = this.fb.group({
      descricao: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  override buscarDados(id: number): void {
    this.service.itemById(id).subscribe((categoria: any) => {
      this.form.patchValue(categoria);
      if (categoria.imagens?.length) {
        this.imagens = categoria.imagens.map((url: string) => ({
          file: null,
          preview: url
        }));
      }
    });
  }

  ngAfterViewInit(): void {
    this.setHeader('Categorias', 'categorias');
  }

  onUpload(event: any): void {
    const file: File = event.files[0];
    const reader = new FileReader();

    reader.onload = () => {
      this.imagens.push({
        file,
        preview: reader.result as string
      });
      // Limpa o input do upload
      if (event && event.originalEvent && event.originalEvent.target) {
        event.originalEvent.target.value = '';
      }
      // Se usar referência do template, pode usar: this.fileUpload.clear();
    };

    reader.readAsDataURL(file);
  }

  visualizarImagem(preview: string): void {
    this.imagemSelecionada = preview;
    this.imagemModalVisivel = true;
  }

  removerImagem(index: number): void {
    this.imagens.splice(index, 1);
  }

  // salvarCategoria(): void {
  //   if (this.form.invalid) return;

  //   const formData = new FormData();
  //   formData.append('nome', this.form.get('nome')?.value);

  //   this.imagens.forEach((img, i) => {
  //     if (img.file) {
  //       formData.append(`imagem${i}`, img.file);
  //     }
  //   });

  //   const sucesso = this.salvar(formData);
  //   if (sucesso) {
  //     console.log('Categoria salva com imagens:', this.form.getRawValue());
  //   }
  // }

  salvarCategoria(): void {
    const categorias: any = this.form.getRawValue();
    const formValid = this.salvar(categorias);

    if (formValid) {
      if (this.idParams)
        this.callBackEditar(categorias)
      else
        this.callBackCriar(categorias);
    }
  }

  callBackCriar(categorias: any): void {
    this.service.insert(categorias).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Categoria criada com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar categoria.' });
        // console.error('Error creating categoria:', err);
      }
    });
  }

  callBackEditar(categorias: any): void {
    this.service.edit(this.idParams, categorias).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Categoria editada com sucesso!' });
        setTimeout(() => this.irParaLista(), 2000);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao editar categoria.' });
        // console.error('Error editing categoria:', err);
      }
    });
  }

  irParaLista(): void {
    this.router.navigate(['/categorias']);
  }
}
