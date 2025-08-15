import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgxMaskDirective, NgxMaskPipe } from 'ngx-mask';
import { NgxPermissionsModule } from 'ngx-permissions';
import { SharedModule } from 'src/app/shared/shared.module';
import { TransportadoraRoutingModule } from './transportadora-routing.module';
import { TransportadoraFormComponent } from './pages/transportadora-form/transportadora-form.component';
import { TransportadoraListComponent } from './pages/transportadora-list/transportadora-list.component';

@NgModule({
  declarations: [
    TransportadoraFormComponent,
    TransportadoraListComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    SharedModule,
    TransportadoraRoutingModule,
    NgxPermissionsModule.forChild(),
    NgxMaskDirective,
    NgxMaskPipe
  ],
})
export class TransportadoraModule {}
