import { Directive, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appMask]',
  standalone: true
})
export class MaskDirective {
  @Input('appMask') maskType: 'cpf' | 'cnpj' | 'telefone' | 'preco' = 'telefone';

  @HostListener('input', ['$event'])
  onInput(event: InputEvent): void {
    const input = event.target as HTMLInputElement;
    const raw = input.value.replace(/\D/g, '');
    const formatted = this.format(raw);

    if (input.value !== formatted) {
      input.value = formatted;
    }
  }

  private format(raw: string): string {
    switch (this.maskType) {
      case 'cpf': return this.formatCpf(raw);
      case 'cnpj': return this.formatCnpj(raw);
      case 'telefone': return this.formatTelefone(raw);
      case 'preco': return this.formatPreco(raw);
      default: return raw;
    }
  }

  private formatCpf(raw: string): string {
    const v = raw.slice(0, 11);
    return v.replace(/^(\d{3})(\d)/, '$1.$2')
            .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
            .replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3-$4');
  }

  private formatCnpj(raw: string): string {
    const v = raw.slice(0, 14);
    return v.replace(/^(\d{2})(\d)/, '$1.$2')
            .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
            .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4')
            .replace(/\/$/, '')
            .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d)/, '$1.$2.$3/$4-$5');
  }

  private formatTelefone(raw: string): string {
    const v = raw.slice(0, 11);
    return v.replace(/^(\d{2})(\d)/, '($1) $2')
            .replace(/^\((\d{2})\) (\d{5})(\d)/, '($1) $2-$3');
  }

  private formatPreco(raw: string): string {
    const v = raw.slice(0, 9);
    const digits = v.replace(/\D/g, '');
    if (!digits) return '';
    const padded = digits.padStart(3, '0');
    const intPart = padded.slice(0, -2).replace(/^0+/, '') || '0';
    const decPart = padded.slice(-2);
    const intFormatted = intPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return intFormatted + ',' + decPart;
  }
}
