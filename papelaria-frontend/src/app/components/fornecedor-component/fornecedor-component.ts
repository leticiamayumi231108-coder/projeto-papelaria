import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FornecedorService } from '../../services/fornecedor-service';
import { Fornecedor } from '../../models/fornecedor';
import { MaskDirective } from '../../directives/mask.directive';

@Component({
  selector: 'app-fornecedor-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaskDirective],
  templateUrl: './fornecedor-component.html',
  styleUrl: './fornecedor-component.scss'
})
export class FornecedorComponent implements OnInit {
  fornecedores: Fornecedor[] = [];
  erro = '';
  editandoId: number | null = null;
  form;

  constructor(
    private fornecedorService: FornecedorService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      nomeFornecedor: ['', [Validators.required, Validators.minLength(3)]],
      cnpj: ['', [Validators.required, Validators.pattern(/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/)]],
      telefoneFornecedor: ['', Validators.required],
      emailFornecedor: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.fornecedorService.listar().subscribe({
      next: (dados) => { this.fornecedores = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar fornecedores: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      Object.values(this.form.controls).forEach(c => c.markAsTouched());
      this.erro = 'Preencha todos os campos corretamente.';
      return;
    }
    this.erro = '';
    const dados = this.form.value as Fornecedor;
    dados.cnpj = dados.cnpj.replace(/\D/g, '');
    dados.telefoneFornecedor = dados.telefoneFornecedor.replace(/\D/g, '');

    if (this.editandoId) {
      this.fornecedorService.atualizar(this.editandoId, dados).subscribe({
        next: () => {
          this.cancelarEdicao();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao atualizar: ' + err.message; }
      });
    } else {
      this.fornecedorService.criar(dados).subscribe({
        next: () => {
          this.form.reset();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao cadastrar: ' + err.message; }
      });
    }
  }

  editar(fornecedor: Fornecedor): void {
    this.editandoId = fornecedor.idFornecedor ?? null;
    const rawCnpj = (fornecedor.cnpj || '').replace(/\D/g, '');
    const rawTel = (fornecedor.telefoneFornecedor || '').replace(/\D/g, '');
    this.form.patchValue({
      nomeFornecedor: fornecedor.nomeFornecedor,
      cnpj: rawCnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5'),
      telefoneFornecedor: rawTel.replace(/^(\d{2})(\d{4,5})(\d{4})$/, '($1) $2-$3'),
      emailFornecedor: fornecedor.emailFornecedor
    });
  }

  fecharErro(): void {
    this.erro = '';
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.form.reset();
  }

  excluir(id: number | undefined): void {
    if (!id || !confirm('Deseja excluir este fornecedor?')) return;
    this.fornecedorService.excluir(id).subscribe({
      next: () => { this.listar(); },
      error: (err) => { this.erro = 'Erro ao excluir: ' + err.message; }
    });
  }
}
