import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../services/usuario-service';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-usuario-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './usuario-component.html',
  styleUrl: './usuario-component.scss'
})
export class UsuarioComponent implements OnInit {
  usuarios: Usuario[] = [];
  erro = '';
  editandoId: number | null = null;
  form;

  constructor(
    private usuarioService: UsuarioService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required],
      acesso: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.usuarioService.listar().subscribe({
      next: (dados) => { this.usuarios = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar usuários: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      Object.values(this.form.controls).forEach(c => c.markAsTouched());
      this.erro = 'Preencha todos os campos corretamente.';
      return;
    }
    this.erro = '';
    const dados = this.form.value as Usuario;

    if (this.editandoId) {
      if (!dados.senha) {
        delete dados.senha;
      }
      this.usuarioService.atualizar(this.editandoId, dados).subscribe({
        next: () => {
          this.cancelarEdicao();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao atualizar: ' + err.message; }
      });
    } else {
      this.usuarioService.criar(dados).subscribe({
        next: () => {
          this.form.reset();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao cadastrar: ' + err.message; }
      });
    }
  }

  editar(usuario: Usuario): void {
    this.editandoId = usuario.idUsuario ?? null;
    this.form.patchValue({
      nome: usuario.nome,
      email: usuario.email,
      senha: '',
      acesso: usuario.acesso
    });
    if (this.editandoId) {
      this.form.get('senha')?.clearValidators();
      this.form.get('senha')?.updateValueAndValidity();
    }
  }

  fecharErro(): void {
    this.erro = '';
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.form.reset();
    this.form.get('senha')?.setValidators(Validators.required);
    this.form.get('senha')?.updateValueAndValidity();
  }

  excluir(id: number | undefined): void {
    if (!id || !confirm('Deseja excluir este usuário?')) return;
    this.usuarioService.excluir(id).subscribe({
      next: () => { this.listar(); },
      error: (err) => { this.erro = 'Erro ao excluir: ' + err.message; }
    });
  }
}
