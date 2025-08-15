import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from '@core/auth/auth-guard.service';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./modules/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [AuthGuardService]
  },
  {
    path: 'login',
    loadComponent: () => import('./modules/auth/pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'cadastro',
    loadComponent: () => import('./modules/auth/pages/cadastro/cadastro.component').then(m => m.CadastroComponent)
  },
  {
    path: 'produtos',
    loadChildren: () => import('./modules/produtos/produtos.module').then(m => m.ProdutoModule)
  },
  {
    path: 'categorias',
    loadChildren: () => import('./modules/categorias/categorias.module').then(m => m.CategoriasModule)
  },
  {
    path: 'fretes',
    loadChildren: () => import('./modules/fretes/fretes.module').then(m => m.FretesModule)
  },
  {
    path: 'usuarios',
    loadChildren: () => import('./modules/usuarios/usuarios.module').then(m => m.UsuariosModule)
  },
  {
    path: 'clientes',
    loadChildren: () => import('./modules/clientes/clientes.module').then(m => m.ClientesModule)
  },
  {
    path: 'pedidos',
    loadChildren: () => import('./modules/pedidos/pedidos.module').then(m => m.PedidosModule)
  },
  {
    path: 'transportadoras',
    loadChildren: () => import('./modules/transportadora/transportadora.module').then(m => m.TransportadoraModule)
  },
  {
    path: 'minha-conta',
    loadChildren: () => import('./modules/clientes/clientes.module').then(m => m.ClientesModule)
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
