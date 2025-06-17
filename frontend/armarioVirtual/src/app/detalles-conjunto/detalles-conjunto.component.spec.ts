import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesConjuntoComponent } from './detalles-conjunto.component';

describe('DetallesConjuntoComponent', () => {
  let component: DetallesConjuntoComponent;
  let fixture: ComponentFixture<DetallesConjuntoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallesConjuntoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesConjuntoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
