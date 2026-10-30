import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../services/cliente-service';
import { Cliente } from '../../models/cliente';
import { MaskDirective } from '../../directives/mask.directive';

@Component({
  selector: 'app-cliente-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaskDirective],
  templateUrl: './cliente-component.html',
  styleUrl: './cliente-component.scss'
})
export class ClienteComponent implements OnInit {
  clientes: Cliente[] = [];
  erro = '';
  editandoId: number | null = null;
  form;

  constructor(
    private clienteService: ClienteService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/)]],
      telefone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.clienteService.listar().subscribe({
      next: (dados) => { this.clientes = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar clientes: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  salvar(): void {
    if (this.form.invalid) return;
    const dados = this.form.value as Cliente;
    dados.cpf = dados.cpf.replace(/\D/g, '');
    dados.telefone = dados.telefone.replace(/\D/g, '');

    if (this.editandoId) {
      this.clienteService.atualizar(this.editandoId, dados).subscribe({
        next: () => {
          this.cancelarEdicao();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao atualizar: ' + err.message; }
      });
    } else {
      this.clienteService.criar(dados).subscribe({
        next: () => {
          this.form.reset();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao cadastrar: ' + err.message; }
      });
    }
  }

  editar(cliente: Cliente): void {
    this.editandoId = cliente.idCliente ?? null;
    const rawCpf = (cliente.cpf || '').replace(/\D/g, '');
    const rawTel = (cliente.telefone || '').replace(/\D/g, '');
    this.form.patchValue({
      nome: cliente.nome,
      cpf: rawCpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, '$1.$2.$3-$4'),
      telefone: rawTel.replace(/^(\d{2})(\d{4,5})(\d{4})$/, '($1) $2-$3'),
      email: cliente.email
    });
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.form.reset();
  }

  excluir(id: number | undefined): void {
    if (!id || !confirm('Deseja excluir este cliente?')) return;
    this.clienteService.excluir(id).subscribe({
      next: () => { this.listar(); },
      error: (err) => { this.erro = 'Erro ao excluir: ' + err.message; }
    });
  }
}
