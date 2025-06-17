// src/app/services/comentario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Comentario {
  id: number;
  contenido: string;
  fechaPublicacion: string;
  cliente: {
    id: string;
    username: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiBase = '/api';

  constructor(private http: HttpClient) { }

  getComentariosDePrenda(prendaId: number): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiBase}/prendas/${prendaId}/comentarios`);
  }

  getComentariosDeConjunto(conjuntoId: number): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiBase}/conjuntos/${conjuntoId}/comentarios`)
  }

  crearComentarioEnConjunto(conjuntoId: number, dto: { contenido: string }): Observable<Comentario> {
    return this.http.post<Comentario>(`/api/conjuntos/${conjuntoId}/comentarios`, dto);
  }
  
  eliminarComentario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/comentarios/${id}`);
  }

  crearComentarioEnPrenda(prendaId: number, dto: { contenido: string }): Observable<Comentario> {
    return this.http.post<Comentario>(`${this.apiBase}/prendas/${prendaId}/comentarios`, dto);
  }
  
}
