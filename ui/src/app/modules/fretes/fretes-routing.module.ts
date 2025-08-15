import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardService } from "@core/auth/auth-guard.service";
import { RouteAction } from "@core/enum/actions.enum";
import { FretesListComponent } from "./pages/fretes-list/fretes-list.component";
import { FretesFormComponent } from "./pages/fretes-form/fretes-form.component";

const routes: Routes = [
  {
    path: '',
    component: FretesListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Fretes',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: FretesFormComponent,
    data: {
      breadcrumb: 'Novo Frete',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar/:id',
    component: FretesFormComponent,
    data: {
      breadcrumb: 'Editar Frete',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FretesRoutingModule { }
