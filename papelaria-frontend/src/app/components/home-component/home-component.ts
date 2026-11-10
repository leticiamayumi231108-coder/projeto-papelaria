import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

interface Funcionalidade {
  titulo: string;
  descricao: string;
  rota: string;
  classe: string;
  svg: string;
}

@Component({
  selector: 'app-home-component',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home-component.html',
  styleUrl: './home-component.scss'
})
export class HomeComponent {
  funcionalidades: Funcionalidade[] = [
    {
      titulo: 'Clientes',
      descricao: 'Cadastro e gerenciamento de clientes',
      rota: '/clientes',
      classe: 'c1',
      svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="8" r="4"/><path d="M4 21c0-4.4 3.6-8 8-8s8 3.6 8 8"/></svg>'
    },
    {
      titulo: 'Fornecedores',
      descricao: 'Cadastro e gerenciamento de fornecedores',
      rota: '/fornecedores',
      classe: 'c2',
      svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="4" y="3" width="16" height="18" rx="1"/><path d="M9 8h1M14 8h1M9 12h1M14 12h1M9 16h1M14 16h1"/></svg>'
    },
    {
      titulo: 'Produtos',
      descricao: 'Cadastro e gerenciamento de produtos',
      rota: '/produtos',
      classe: 'c3',
      svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M3 8l9-5 9 5-9 5-9-5z"/><path d="M3 8v8l9 5 9-5V8"/><path d="M12 13v8"/></svg>'
    },
    {
      titulo: 'Usuários',
      descricao: 'Cadastro e gerenciamento de usuários',
      rota: '/usuarios',
      classe: 'c4',
      svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="8" r="3.2"/><circle cx="16.5" cy="9.5" r="2.6"/><path d="M3 20c0-3.6 2.7-6.5 6-6.5s6 2.9 6 6.5"/><path d="M15 14.2c2.6.3 4.5 2.7 4.5 5.8"/></svg>'
    },
    {
      titulo: 'Vendas',
      descricao: 'Realização de vendas',
      rota: '/vendas',
      classe: 'c5',
      svg: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="20" r="1.3"/><circle cx="17" cy="20" r="1.3"/><path d="M2.5 4h2.5l2.2 11.2a2 2 0 0 0 2 1.6h7.7a2 2 0 0 0 2-1.6L21 7H6"/></svg>'
    }
  ];
}
