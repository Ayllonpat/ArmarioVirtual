import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiArmarioComponent } from './mi-armario.component';

describe('MiArmarioComponent', () => {
  let component: MiArmarioComponent;
  let fixture: ComponentFixture<MiArmarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiArmarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MiArmarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
