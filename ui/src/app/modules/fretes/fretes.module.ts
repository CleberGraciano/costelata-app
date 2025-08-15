import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { NgxPermissionsModule } from "ngx-permissions";
import { SharedModule } from "src/app/shared/shared.module";
import { FretesRoutingModule } from "./fretes-routing.module";
import { FretesFormComponent } from "./pages/fretes-form/fretes-form.component";
import { FretesListComponent } from "./pages/fretes-list/fretes-list.component";

@NgModule({
  declarations: [
    FretesListComponent,
    FretesFormComponent
  ],
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    FretesRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
  providers:[

  ]
})
export class FretesModule { }
