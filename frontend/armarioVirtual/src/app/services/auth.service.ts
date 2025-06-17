import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Usuario {
  id?: string;
  nombre?: string;
  username: string;
  email?: string;
  prendas?: any[];
  conjuntos?: any[];
  seguidores?: any[];
  seguidos?: any[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  getPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>('/api/usuarios/me');
  }

  getSeguidores(id: string) {
  return this.http.get<any>(`/api/usuarios/${id}/seguidores`);
}

getSeguidos(id: string) {
  return this.http.get<any>(`/api/usuarios/${id}/seguidos`);
}

follow(id: string) {
  return this.http.post(`/api/usuarios/${id}/follow`, {});
}

unfollow(id: string) {
  return this.http.post(`/api/usuarios/${id}/unfollow`, {});
}

  
}
