import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { RouteAction } from "@core/enum/actions.enum";
import { CategoriasListComponent } from "./pages/categorias-list/categorias-list.component";
import { CategoriasFormComponent } from "./pages/categorias-form/categorias-form.component";

const routes: Routes = [
  {
    path: '',
    component: CategoriasListComponent,
    // canActivate:[AuthGuardService],
    data: {
      breadcrumb: 'Listar Categorias',
      action: RouteAction.Filtrar
      // permissions: {
      //   only: PermissoesAcesso.Todas,
      //   redirectTo: '/'
      // }
    }
  },
  {
    path: 'cadastrar',
    component: CategoriasFormComponent,
    data: {
      breadcrumb: 'Nova Categoria',
      action: RouteAction.Cadastrar
    }
  },
  {
    path: 'editar/:id',
    component: CategoriasFormComponent,
    data: {
      breadcrumb: 'Editar Categoria',
      action: RouteAction.Editar
    }
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoriasRoutingModule { }
