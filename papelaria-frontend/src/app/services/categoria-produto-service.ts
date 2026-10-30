import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CategoriaProduto } from '../models/categoria-produto';

@Injectable({ providedIn: 'root' })
export class CategoriaProdutoService {
    private apiUrl = `${environment.apiUrl}/categorias-produto`;

  constructor(private http: HttpClient) {}

  listar(): Observable<CategoriaProduto[]> {
    return this.http.get<CategoriaProduto[]>(this.apiUrl);
  }
}
