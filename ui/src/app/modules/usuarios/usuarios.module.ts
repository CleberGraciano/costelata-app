import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { NgxPermissionsModule } from "ngx-permissions";
import { SharedModule } from "src/app/shared/shared.module";
import { UsuariosListComponent } from "./pages/usuarios-list/usuarios-list.component";
import { UsuariosFormComponent } from "./pages/usuarios-form/usuarios-form.component";
import { UsuariosRoutingModule } from "./usuarios-routing.module";

@NgModule({
  declarations: [
    UsuariosListComponent,
    UsuariosFormComponent
  ],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    UsuariosRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers:[

  ]
})
export class UsuariosModule { }
