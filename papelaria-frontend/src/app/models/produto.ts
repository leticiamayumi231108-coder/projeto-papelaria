export interface Mercadoria {
    idProduto?: number;
    nomeProduto: string;
    descricao: string;
    idCategoriaProduto: number;
    nomeCategoria?: string;
    preco: number;
    estoque: number;
    dataValidade?: string;
}
