import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProdutoService } from '../../services/produto-service';
import { CategoriaProdutoService } from '../../services/categoria-produto-service';
import { Produto } from '../../models/produto';
import { CategoriaProduto } from '../../models/categoria-produto';
import { MaskDirective } from '../../directives/mask.directive';

@Component({
  selector: 'app-produto-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaskDirective],
  templateUrl: './produto-component.html',
  styleUrl: './produto-component.scss'
})
export class ProdutoComponent implements OnInit {
  produtos: Produto[] = [];
  categorias: CategoriaProduto[] = [];
  erro = '';
  editandoId: number | null = null;
  form;

  constructor(
    private produtoService: ProdutoService,
    private categoriaService: CategoriaProdutoService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      nomeProduto: ['', [Validators.required, Validators.minLength(3)]],
      descricao: [''],
      idCategoriaProduto: ['', Validators.required],
      preco: ['', [Validators.required, Validators.min(0.01)]],
      estoque: ['', [Validators.required, Validators.min(0)]],
      dataValidade: ['']
    });
  }

  ngOnInit(): void {
    this.carregarCategorias();
    this.listar();
  }

  carregarCategorias(): void {
    this.categoriaService.listar().subscribe({
      next: (dados) => { this.categorias = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar categorias: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  listar(): void {
    this.produtoService.listar().subscribe({
      next: (dados) => { this.produtos = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar produtos: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      Object.values(this.form.controls).forEach(c => c.markAsTouched());
      this.erro = 'Preencha todos os campos corretamente.';
      return;
    }
    this.erro = '';
    const v = this.form.value;
    const dados: Produto = {
      nomeProduto: v.nomeProduto ?? '',
      descricao: v.descricao ?? '',
      idCategoriaProduto: Number(v.idCategoriaProduto),
      preco: Number(String(v.preco).replace(/\./g, '').replace(',', '.')),
      estoque: Number(v.estoque),
    };
    if (v.dataValidade) {
      dados.dataValidade = v.dataValidade;
    }

    if (this.editandoId) {
      this.produtoService.atualizar(this.editandoId, dados).subscribe({
        next: () => {
          this.cancelarEdicao();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao atualizar: ' + err.message; }
      });
    } else {
      this.produtoService.criar(dados).subscribe({
        next: () => {
          this.form.reset();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao cadastrar: ' + err.message; }
      });
    }
  }

  editar(produto: Produto): void {
    this.editandoId = produto.idProduto ?? null;
    const precoStr = String(produto.preco).replace(/\D/g, '');
    const padded = precoStr.padStart(3, '0');
    const intPart = padded.slice(0, -2).replace(/^0+/, '') || '0';
    const preco = intPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + ',' + padded.slice(-2);
    this.form.patchValue({
      nomeProduto: produto.nomeProduto,
      descricao: produto.descricao,
      idCategoriaProduto: String(produto.idCategoriaProduto),
      preco: preco,
      estoque: String(produto.estoque),
      dataValidade: produto.dataValidade ? produto.dataValidade.substring(0, 10) : ''
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
    if (!id || !confirm('Deseja excluir este produto?')) return;
    this.produtoService.excluir(id).subscribe({
      next: () => { this.listar(); },
      error: (err) => { this.erro = 'Erro ao excluir: ' + err.message; }
    });
  }
}
