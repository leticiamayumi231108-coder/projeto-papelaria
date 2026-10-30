import { ItemVenda } from './item-venda';

export interface Venda {
    idVenda?: number;
    dataVenda?: string;
    valorTotal?: number;
    formaPagamento: string;
    idCliente: number;
    nomeCliente?: string;
    itens: ItemVenda[];
}
