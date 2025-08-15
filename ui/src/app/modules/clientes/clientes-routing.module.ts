import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardService } from "@core/auth/auth-guard.service";
import { RouteAction } from "@core/enum/actions.enum";
import { ClientesListComponent } from "./pages/clientes-list/clientes-list.component";
import { ClientesFormComponent } from "./pages/clientes-form/clientes-form.component";

const routes: Routes = [
  {
    path: '',
    component: ClientesListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Clientes',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: ClientesFormComponent,
    data: {
      breadcrumb: 'Novo Cliente',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar',
    component: ClientesFormComponent,
    data: {
      breadcrumb: 'Editar Cliente',
      action: RouteAction.Editar
    }
  },
  {
    path: 'configuracoes',
    component: ClientesFormComponent,
    data: {
      breadcrumb: 'Meus Dados',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientesRoutingModule { }
