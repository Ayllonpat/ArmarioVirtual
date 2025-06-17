import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubirPrendaComponent } from './subir-prenda.component';

describe('SubirPrendaComponent', () => {
  let component: SubirPrendaComponent;
  let fixture: ComponentFixture<SubirPrendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubirPrendaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubirPrendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
