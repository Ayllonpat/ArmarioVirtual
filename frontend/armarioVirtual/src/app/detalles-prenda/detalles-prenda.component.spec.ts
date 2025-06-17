import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesPrendaComponent } from './detalles-prenda.component';

describe('DetallesPrendaComponent', () => {
  let component: DetallesPrendaComponent;
  let fixture: ComponentFixture<DetallesPrendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallesPrendaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesPrendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
