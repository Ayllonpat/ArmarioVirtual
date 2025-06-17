import { TestBed } from '@angular/core/testing';

import { TipoPrendaService } from './tipo-prenda.service';

describe('TipoPrendaService', () => {
  let service: TipoPrendaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoPrendaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
