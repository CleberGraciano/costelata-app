import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { NgxPermissionsModule } from "ngx-permissions";
import { SharedModule } from "src/app/shared/shared.module";
import { ProdutosRoutingModule } from "./produtos-routing.module";
import { ProdutosFormComponent } from "./pages/produtos-form/produtos-form.component";
import { ProdutosListComponent } from "./pages/produtos-list/produtos-list.component";

@NgModule({
  declarations: [
    ProdutosFormComponent,
    ProdutosListComponent
  ],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    ProdutosRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers:[

  ]
})
export class ProdutoModule { }
