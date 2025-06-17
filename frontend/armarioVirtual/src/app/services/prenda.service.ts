import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Prenda {
  id: number;
  nombre: string;
  imagenUrl: string;
  color: string;
  talla: string;
  enlaceCompra: string;
  visibilidad: string;
  tipoPrendaId?: number;
  cliente: {
    id: string;
    username: string;
  };
  fechaPublicacion: string;
  tags?: Array<{ id: number; nombre: string }>; 
}


@Injectable({ providedIn: 'root' })
export class PrendaService {
  private apiUrl = '/api/prendas';

  constructor(private http: HttpClient) {}

  getTipos(): Observable<{ id: number; nombre: string }[]> {
    return this.http.get<{ id: number; nombre: string }[]>('/api/tipos-prenda');
  }

  createPrenda(dto: any): Observable<Prenda> {
    return this.http.post<Prenda>(`${this.apiUrl}/añadir`, dto);
  }

  uploadImage(prendaId: number, file: File): Observable<void> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<void>(`${this.apiUrl}/${prendaId}/imagen`, form);
  }

  getById(id: number): Observable<Prenda> {
    return this.http.get<Prenda>(`${this.apiUrl}/${id}`);
  }
  
  toggleLike(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/like`, {});
  }
  
  getLikeCount(id: number): Observable<any> {
    return this.http.get<number>(`${this.apiUrl}/${id}/likes`);
  }  
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
}