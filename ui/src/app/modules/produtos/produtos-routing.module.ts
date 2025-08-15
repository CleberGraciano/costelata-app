import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardService } from "@core/auth/auth-guard.service";
import { RouteAction } from "@core/enum/actions.enum";
import { ProdutosListComponent } from "./pages/produtos-list/produtos-list.component";
import { ProdutosFormComponent } from "./pages/produtos-form/produtos-form.component";

const routes: Routes = [
  {
    path: '',
    component: ProdutosListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Produtos',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: ProdutosFormComponent,
    data: {
      breadcrumb: 'Novo Produto',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar',
    component: ProdutosFormComponent,
    data: {
      breadcrumb: 'Editar Produto',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProdutosRoutingModule { }
