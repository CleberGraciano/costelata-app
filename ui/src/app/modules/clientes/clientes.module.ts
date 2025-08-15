import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { NgxPermissionsModule } from "ngx-permissions";
import { SharedModule } from "src/app/shared/shared.module";
import { ClientesRoutingModule } from "./clientes-routing.module";
import { ClientesListComponent } from "./pages/clientes-list/clientes-list.component";
import { ClientesFormComponent } from "./pages/clientes-form/clientes-form.component";

@NgModule({
  declarations: [
    ClientesListComponent,
    ClientesFormComponent
  ],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    ClientesRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers:[

  ]
})
export class ClientesModule { }
