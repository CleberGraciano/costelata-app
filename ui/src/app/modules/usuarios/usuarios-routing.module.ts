import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardService } from "@core/auth/auth-guard.service";
import { RouteAction } from "@core/enum/actions.enum";
import { UsuariosFormComponent } from "./pages/usuarios-form/usuarios-form.component";
import { UsuariosListComponent } from "./pages/usuarios-list/usuarios-list.component";

const routes: Routes = [
  {
    path: '',
    component: UsuariosListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Usuários',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: UsuariosFormComponent,
    data: {
      breadcrumb: 'Novo Usuário',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar/:id',
    component: UsuariosFormComponent,
    data: {
      breadcrumb: 'Editar Usuário',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuariosRoutingModule { }
