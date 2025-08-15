import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PedidosListComponent } from './pages/pedidos-list/pedidos-list.component';
import { RouteAction } from '@core/enum/actions.enum';
import { PedidosFormComponent } from './pages/pedidos-form/pedidos-form.component';
import { PedidoMobileComponent } from './pages/pedido-mobile/pedido-mobile.component';
import { ReciboPedidoComponent } from './pages/recibo-pedido/recibo-pedido.component';

const routes: Routes = [
  {
    path: '',
    component: PedidosListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Pedidos',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: PedidosFormComponent,
    data: {
      breadcrumb: 'Novo Pedido',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar/:id',
    component: PedidosFormComponent,
    data: {
      breadcrumb: 'Editar Pedido',
      action: RouteAction.Editar
    }
  },
  {
    path: 'fazer-meu-pedido',
    component: PedidoMobileComponent,
    data: {
      breadcrumb: 'Pedido Mobile',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'recibo/:id',
    component: ReciboPedidoComponent,
    data: {
      breadcrumb: 'Recibo do Pedido',
      action: RouteAction.Visualizar
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PedidosRoutingModule {}
