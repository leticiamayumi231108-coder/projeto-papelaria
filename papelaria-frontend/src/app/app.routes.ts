import { Routes } from '@angular/router';
import { HomeComponent } from './components/home-component/home-component';
import { ClienteComponent } from './components/cliente-component/cliente-component';
import { FornecedorComponent } from './components/fornecedor-component/fornecedor-component';
import { ProdutoComponent } from './components/produto-component/produto-component';
import { UsuarioComponent } from './components/usuario-component/usuario-component';
import { VendaComponent } from './components/venda-component/venda-component';
import { LoginComponent } from './components/login-component/login-component';
import { AuthService } from './services/auth-service';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

// bloqueia o acesso quando não há login.
function authGuard() {
  const auth = inject(AuthService);
  if (auth.isAuthenticated()) return true;
  const router = inject(Router);
  return router.parseUrl('/login');
}

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', pathMatch: 'full', component: HomeComponent, canActivate: [authGuard] },
  { path: 'clientes', component: ClienteComponent, canActivate: [authGuard] },
  { path: 'fornecedores', component: FornecedorComponent, canActivate: [authGuard] },
  { path: 'produtos', component: ProdutoComponent, canActivate: [authGuard] },
  { path: 'usuarios', component: UsuarioComponent, canActivate: [authGuard] },
  { path: 'vendas', component: VendaComponent, canActivate: [authGuard] }
];
