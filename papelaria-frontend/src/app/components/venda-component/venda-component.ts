/*
 * [REGRAS DE ESTOQUE]
 * - O campo `erroEstoque` em cada item da venda é verificado em tempo real:
 *   ao selecionar um produto ou alterar a quantidade, valida se a quantidade
 *   solicitada não excede o estoque disponível.
 * - Se houver erro em qualquer item, o botão "Finalizar Venda" fica desabilitado.
 * - O backend também valida o estoque ao salvar (VendaService), garantindo
 *   a integridade mesmo que a validação do frontend seja contornada.
 */
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { VendaService } from '../../services/venda-service';
import { ClienteService } from '../../services/cliente-service';
import { ProdutoService } from '../../services/produto-service';
import { Venda } from '../../models/venda';
import { ItemVenda } from '../../models/item-venda';
import { Cliente } from '../../models/cliente';
import { Produto } from '../../models/produto';

@Component({
  selector: 'app-venda-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './venda-component.html',
  styleUrl: './venda-component.scss'
})
export class VendaComponent implements OnInit {
  vendas: Venda[] = [];
  clientes: Cliente[] = [];
  produtos: Produto[] = [];
  erro = '';
  editandoId: number | null = null;
  form;
  /* `erroEstoque` em cada item é preenchido pela validação em onProdutoChange / onQuantidadeChange */
  itensForm: (ItemVenda & { erroEstoque?: string })[] = [];
  totalVenda = 0;
  /* Impede o salvamento se houver qualquer item com estoque insuficiente */
  temErroEstoque = false;

  readonly formasPagamento = [
    { valor: 'DINHEIRO', rotulo: 'Dinheiro' },
    { valor: 'CARTAO_CREDITO', rotulo: 'Cartão de Crédito' },
    { valor: 'CARTAO_DEBITO', rotulo: 'Cartão de Débito' },
    { valor: 'PIX', rotulo: 'PIX' }
  ];

  constructor(
    private vendaService: VendaService,
    private clienteService: ClienteService,
    private produtoService: ProdutoService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      idCliente: ['', Validators.required],
      formaPagamento: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.carregarClientes();
    this.carregarProdutos();
    this.listar();
  }

  carregarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (dados) => { this.clientes = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar clientes: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  carregarProdutos(): void {
    this.produtoService.listar().subscribe({
      next: (dados) => { this.produtos = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar produtos: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  listar(): void {
    this.vendaService.listar().subscribe({
      next: (dados) => { this.vendas = dados; this.cdr.markForCheck(); },
      error: (err) => { this.erro = 'Erro ao carregar vendas: ' + err.message; this.cdr.markForCheck(); }
    });
  }

  /* Adiciona um item vazio; o select de produto forçará a validação ao ser alterado */
  adicionarItem(): void {
    this.itensForm.push({ idProduto: 0, quantidade: 1, erroEstoque: '' });
  }

  removerItem(index: number): void {
    this.itensForm.splice(index, 1);
    this.calcularTotal();
    this.validarTodosItens(); /* [ESTOQUE] Revalida erros após remover */
  }

  onProdutoSelect(index: number, value: string): void {
    this.itensForm[index].idProduto = Number(value);
    this.onProdutoChange(index);
  }

  /* [ESTOQUE] Valida a quantidade digitada e exibe erro se exceder o estoque disponível */
  onQuantidadeChange(index: number, value: string): void {
    this.itensForm[index].quantidade = Number(value);
    this.calcularTotal();
    this.validarEstoqueItem(index);
  }

  /* [ESTOQUE] Ao trocar o produto, atualiza preço/nome e valida o estoque */
  onProdutoChange(index: number): void {
    const item = this.itensForm[index];
    const produto = this.produtos.find(p => p.idProduto === item.idProduto);
    if (produto) {
      item.precoUnitario = produto.preco;
      item.nomeProduto = produto.nomeProduto;
    }
    this.calcularTotal();
    this.validarEstoqueItem(index);
  }

  /* [ESTOQUE] Verifica se a quantidade solicitada não excede o estoque disponível */
  private validarEstoqueItem(index: number): void {
    const item = this.itensForm[index];
    if (!item.idProduto) {
      item.erroEstoque = '';
      this.validarTodosItens();
      return;
    }
    const produto = this.produtos.find(p => p.idProduto === item.idProduto);
    if (!produto) {
      item.erroEstoque = 'Produto não encontrado.';
      this.validarTodosItens();
      return;
    }
    if (item.quantidade > produto.estoque) {
      item.erroEstoque = `Estoque insuficiente. Disponível: ${produto.estoque}, Solicitado: ${item.quantidade}.`;
    } else {
      item.erroEstoque = '';
    }
    this.validarTodosItens();
  }

  /* [ESTOQUE] Atualiza a flag temErroEstoque (usada para desabilitar o botão salvar) */
  private validarTodosItens(): void {
    this.temErroEstoque = this.itensForm.some(i => !!i.erroEstoque);
  }

  calcularTotal(): void {
    this.totalVenda = this.itensForm.reduce((acc, item) => {
      return acc + (item.precoUnitario || 0) * item.quantidade;
    }, 0);
  }

  /* [ESTOQUE] Bloqueia o salvamento se algum item violar a regra de estoque */
  salvar(): void {
    if (this.form.invalid || this.itensForm.length === 0 || this.temErroEstoque) {
      Object.values(this.form.controls).forEach(c => c.markAsTouched());
      if (this.temErroEstoque) {
        this.erro = 'Corrija os erros de estoque antes de finalizar.';
      } else if (this.itensForm.length === 0) {
        this.erro = 'Adicione pelo menos um item à venda.';
      } else {
        this.erro = 'Preencha todos os campos corretamente.';
      }
      return;
    }
    this.erro = '';
    const dados: Venda = {
      idCliente: Number(this.form.value.idCliente),
      formaPagamento: this.form.value.formaPagamento || '',
      itens: this.itensForm.map(item => ({
        idProduto: item.idProduto,
        quantidade: item.quantidade
      }))
    };

    if (this.editandoId) {
      this.vendaService.atualizar(this.editandoId, dados).subscribe({
        next: () => {
          this.cancelarEdicao();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao atualizar: ' + err.message; }
      });
    } else {
      this.vendaService.criar(dados).subscribe({
        next: () => {
          this.limparForm();
          this.listar();
        },
        error: (err) => { this.erro = 'Erro ao cadastrar: ' + err.message; }
      });
    }
  }

  /* Ao editar, o estoque pode ter sido alterado desde o salvamento; revalida todos os itens */
  editar(venda: Venda): void {
    this.editandoId = venda.idVenda ?? null;
    this.form.patchValue({
      idCliente: String(venda.idCliente),
      formaPagamento: venda.formaPagamento
    });
    this.itensForm = (venda.itens || []).map(item => ({
      ...item,
      idProduto: item.idProduto,
      erroEstoque: ''
    }));
    this.calcularTotal();
    this.itensForm.forEach((_, i) => this.validarEstoqueItem(i));
  }

  fecharErro(): void {
    this.erro = '';
  }

  cancelarEdicao(): void {
    this.editandoId = null;
    this.form.reset();
    this.itensForm = [];
    this.totalVenda = 0;
  }

  limparForm(): void {
    this.form.reset();
    this.itensForm = [];
    this.totalVenda = 0;
  }

  excluir(id: number | undefined): void {
    if (!id || !confirm('Deseja excluir esta venda?')) return;
    this.vendaService.excluir(id).subscribe({
      next: () => { this.listar(); },
      error: (err) => { this.erro = 'Erro ao excluir: ' + err.message; }
    });
  }

  get nomeProduto(): (id: number) => string {
    return (id: number) => this.produtos.find(p => p.idProduto === id)?.nomeProduto ?? '';
  }
}
