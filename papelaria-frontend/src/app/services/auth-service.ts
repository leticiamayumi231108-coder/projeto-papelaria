import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest } from '../models/login-request';
import { Usuario } from '../models/usuario';

const STORAGE_KEY = 'usuarioLogado';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = `${environment.apiUrl}/usuarios`;

    constructor(private http: HttpClient) {}

    login(credenciais: LoginRequest): Observable<Usuario> {
        return this.http.post<Usuario>(`${this.apiUrl}/login`, credenciais)
            .pipe(tap(usuario => localStorage.setItem(STORAGE_KEY, JSON.stringify(usuario))));
    }

    logout(): void {
        localStorage.removeItem(STORAGE_KEY);
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem(STORAGE_KEY);
    }

    getUsuarioLogado(): Usuario | null {
        const raw = localStorage.getItem(STORAGE_KEY);
        return raw ? JSON.parse(raw) : null;
    }
}
