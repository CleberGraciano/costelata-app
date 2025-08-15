import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { NgxPermissionsModule } from "ngx-permissions";
import { SharedModule } from "src/app/shared/shared.module";
import { CategoriasFormComponent } from "./pages/categorias-form/categorias-form.component";
import { CategoriasListComponent } from "./pages/categorias-list/categorias-list.component";
import { CategoriasRoutingModule } from "./categorias-routing.module";

@NgModule({
  declarations: [
    CategoriasFormComponent,
    CategoriasListComponent
  ],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    CategoriasRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers:[

  ]
})
export class CategoriasModule { }
