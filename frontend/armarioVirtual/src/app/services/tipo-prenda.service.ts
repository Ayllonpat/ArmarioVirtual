import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface GetTipoPrendaDto {
  id: number;
  nombre: string;
  tipoPrendaPadre: string | null;
  tipoPrendasHijas: GetTipoPrendaDto[];
}

@Injectable({ providedIn: 'root' })
export class TipoPrendaService {
  private apiUrl = '/api/tipos-prenda';

  constructor(private http: HttpClient) {}

  getTipos(): Observable<GetTipoPrendaDto[]> {
    return this.http.get<GetTipoPrendaDto[]>(this.apiUrl);
  }
}