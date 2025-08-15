import { Component, HostListener, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { UsuariosService } from '@modules/usuarios/services/usuarios.service';
import { MessageService } from 'primeng/api';
import { AuthenticationService } from '@core/auth/authentication.service';

interface CarrinhoItem {
  nome: string;
  quantidade: number;
  preco: number;
}

export interface MenuItem {
  label: string;
  route?: string;
  icon?: string;
  children?: MenuItem[];
  show?: boolean;
  requiresLogin?: boolean;
  logout?: boolean;
  roles?: string[];
  status?: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  displayAlterarSenha = false;
  menuOriginal: MenuItem[] = [
    { label: 'Início', route: '/', roles: ['ADMIN'], status: 'Ativo' },
    { label: 'Início', route: '/pedidos/fazer-meu-pedido', roles: ['USER'], status: 'Ativo' },
    { label: 'Pedidos', route: '/pedidos', roles: ['ADMIN', 'USER'], status: 'Ativo' },
    {
      label: 'Produtos',
      roles: ['ADMIN'],
      status: 'Ativo',
      children: [
        { label: 'Todos os Produtos', route: '/produtos' },
        { label: 'Categorias', route: '/categorias' },
        { label: 'Fretes', route: '/fretes' },
        { label: 'Transportadoras', route: '/transportadoras' }
      ]
    },
    {
      label: 'Administração',
      roles: ['ADMIN'],
      status: 'Ativo',
      children: [
        { label: 'Usuários', route: '/usuarios' },
        { label: 'Clientes', route: '/clientes' }
      ]
    },
    { label: 'Minha Conta', route: '/minha-conta/configuracoes', roles: ['USER'] },
    { label: 'Sair', logout: true, requiresLogin: true }
  ];
  
  menu: MenuItem[] = [];
  submenuState: { [key: string]: boolean } = {};
  usuarioLogado: any;
  menuAberto = false;
  isLargeScreen = window.innerWidth >= 1024;
  mostrarCarrinho = false;
  carrinho: CarrinhoItem[] = [];
  statusUsuario!: any;
  isLoggedIn$ = this.authenticationService.isLoggedIn$;
  private authSub: Subscription | undefined;

  constructor(
    private router: Router,
    private usuariosService: UsuariosService,
    private messageService: MessageService,
    private authenticationService: AuthenticationService
  ) {
    this.menuAberto = this.isLargeScreen;
    const carrinhoLocal = localStorage.getItem('carrinho');
    if (!carrinhoLocal || carrinhoLocal === '[]') {
      const itemFicticio: CarrinhoItem[] = [
        { nome: 'Costela BBQ', quantidade: 2, preco: 59.9 }
      ];
      localStorage.setItem('carrinho', JSON.stringify(itemFicticio));
    }
    this.carregarCarrinho();
  }

  ngOnInit(): void {
    this.authSub = this.isLoggedIn$.subscribe(isLogged => {
      if (isLogged) {
        this.usuarioLogado = this.authenticationService.getUsuarioLogado();
        this.atualizarMenu();
        this.displayAlterarSenha = !!this.usuarioLogado?.senhaProvisoria;
      } else {
        this.usuarioLogado = null;
        this.menu = [];
      }
    });
  }

  ngOnDestroy(): void {
    this.authSub?.unsubscribe();
  }

  atualizarMenu() {
    const usuario = this.authenticationService.getUsuarioLogado();
    const status = usuario?.status;
    const roles = Array.isArray(usuario?.role) ? usuario.role : [usuario?.role];
    console.log(roles);
    this.menu = this.menuOriginal
      .filter(item => this.itemPermitidoMultiRole(item, status, roles))
      .map(item => {
        if (item.children) {
          return {
            ...item,
            children: item.children.filter(sub => this.itemPermitidoMultiRole(sub, status, roles))
          };
        }
        return item;
      });

  }

  itemPermitidoMultiRole(item: MenuItem, status: string, roles: string[]): boolean {
    const statusOk = !item.status || item.status === status;
    const roleOk = !item.roles || item.roles.some(r => roles.includes(r));
    return statusOk && roleOk;
  }

  onSalvarNovaSenha({ senhaProvisoria, novaSenha }: { senhaProvisoria: string, novaSenha: string }) {
    let objEnvio = { 
      senhaProvisoria: senhaProvisoria, 
      novaSenha: novaSenha 
    };

    this.usuariosService.redefinirSenha(objEnvio).subscribe({
     next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Senha alterada com sucesso!' });
        sessionStorage.setItem('usuario', JSON.stringify({ ...this.usuarioLogado, senhaProvisoria: null }));
        this.displayAlterarSenha = false;
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao alterar senha.' });
        this.displayAlterarSenha = true;
        // console.error('Error creating user:', err);
      }
    });
  }

  @HostListener('window:resize')
  onResize() {
    this.isLargeScreen = window.innerWidth >= 1024;
    if (this.isLargeScreen) {
      this.menuAberto = true;
    }
  }

  toggleMenu() {
    this.menuAberto = !this.menuAberto;
  }

  abrirCarrinho() {
    this.mostrarCarrinho = true;
  }

  fecharCarrinho() {
    this.mostrarCarrinho = false;
  }

  carregarCarrinho() {
    const dados = localStorage.getItem('carrinho');
    this.carrinho = dados ? JSON.parse(dados) : [];
  }

  alterarQuantidade(index: number, delta: number) {
    const item = this.carrinho[index];
    item.quantidade += delta;

    if (item.quantidade <= 0) {
      this.carrinho.splice(index, 1);
    }

    this.atualizarCarrinho();
  }

  removerItem(index: number) {
    this.carrinho.splice(index, 1);
    this.atualizarCarrinho();
  }

  finalizarCompra() {
    // alert('Pedido finalizado com sucesso!');
    this.carrinho = [];
    localStorage.removeItem('carrinho');
    this.fecharCarrinho();
    this.router.navigate(['/pedidos/recibo', 1]);
  }

  atualizarCarrinho() {
    localStorage.setItem('carrinho', JSON.stringify(this.carrinho));
  }

  get totalCarrinho(): number {
    return this.carrinho.reduce((total, item) => total + item.preco * item.quantidade, 0);
  }

  logout() {
    this.authenticationService.logout();
  }
}
