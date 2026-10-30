import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.scss'
})
export class LoginComponent {
  erro = '';
  form;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  logar(): void {
    if (this.form.invalid) {
      Object.values(this.form.controls).forEach(c => c.markAsTouched());
      return;
    }
    this.erro = '';
    const { email, senha } = this.form.value;
    this.authService.login({ email: email ?? '', senha: senha ?? '' }).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => {
        this.erro = err.status === 401
          ? 'E-mail ou senha incorretos.'
          : 'Erro ao conectar: ' + (err.error?.message || err.message);
      }
    });
  }
}
