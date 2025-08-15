import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { PedidosListComponent } from './pages/pedidos-list/pedidos-list.component';
import { PedidosFormComponent } from './pages/pedidos-form/pedidos-form.component';
import { PedidosRoutingModule } from './pedidos-routing.module';
import { PedidoMobileComponent } from './pages/pedido-mobile/pedido-mobile.component';
import { ReciboPedidoComponent } from './pages/recibo-pedido/recibo-pedido.component';
import { NgxPermissionsModule } from 'ngx-permissions';

@NgModule({
  declarations: [
    PedidosListComponent,
    PedidosFormComponent,
    PedidoMobileComponent,
    ReciboPedidoComponent
  ],
  imports: [
    SharedModule,
    PedidosRoutingModule,
    NgxPermissionsModule.forChild()
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosModule {}
