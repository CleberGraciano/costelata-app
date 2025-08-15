import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransportadoraListComponent } from './pages/transportadora-list/transportadora-list.component';
import { TransportadoraFormComponent } from './pages/transportadora-form/transportadora-form.component';
import { RouteAction } from '@core/enum/actions.enum';

const routes: Routes = [
  {
    path: '',
    component: TransportadoraListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Transportadoras',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: TransportadoraFormComponent,
    data: {
      breadcrumb: 'Nova Transportadora',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar/:id',
    component: TransportadoraFormComponent,
    data: {
      breadcrumb: 'Editar Transportadora',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransportadoraRoutingModule {}
