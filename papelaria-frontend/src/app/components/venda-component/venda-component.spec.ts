import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VendaComponent } from './venda-component';

describe('VendaComponent', () => {
  let component: VendaComponent;
  let fixture: ComponentFixture<VendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VendaComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
