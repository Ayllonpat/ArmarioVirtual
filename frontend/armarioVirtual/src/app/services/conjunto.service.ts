import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Conjunto {
  id: number;
  nombre: string;
  imagenUrl: string;
  fechaPublicacion: string;
  prendas: Array<{ id: number; nombre: string; imagenUrl: string }>;
  cliente: {      
    id: number;
    username: string;
  };
  visibilidad: string;
  tags: Array<{ id: number; nombre: string }>; 
}

export interface Comentario {
  id: number;
  contenido: string;
  fechaPublicacion: string;
  cliente: { username: string };
}

export interface DetalleConjunto {
  conjunto: Conjunto;
  likes: number;
  comentarios: Comentario[];
}

@Injectable({ providedIn: 'root' })
export class ConjuntoService {
  private apiUrl = '/api/conjuntos';

  constructor(private http: HttpClient) {}

  getPrendas(): Observable<{ id: number; nombre: string }[]> {
    return this.http.get<{ id: number; nombre: string }[]>('/api/prendas/cliente');
  }

  createConjunto(dto: any): Observable<Conjunto> {
    return this.http.post<Conjunto>(`${this.apiUrl}/añadir`, dto);
  }

  uploadImage(conjuntoId: number, file: File): Observable<void> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<void>(`${this.apiUrl}/${conjuntoId}/imagen`, form);
  }

  getDetalleConjunto(id: number): Observable<DetalleConjunto> {
    return forkJoin({
      conjunto: this.http.get<Conjunto>(`${this.apiUrl}/${id}`),
      likes: this.http.get<number>(`${this.apiUrl}/${id}/likes`),
      comentarios: this.http.get<Comentario[]>(`${this.apiUrl}/${id}/comentarios`)
    });
  }

  toggleLike(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/like`, {});
  }
  
  getLikeCount(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/likes`);
  }

  deleteConjunto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  
}
