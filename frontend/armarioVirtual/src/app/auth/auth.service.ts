import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}
  login(creds: { username: string; password: string }) {
    return this.http
      .post<{ token: string }>('/api/usuarios/auth/login', creds)
      .pipe(tap(res => localStorage.setItem('token', res.token)));
  }
  register(data: any) {
    return this.http.post('/api/usuarios/crear/cliente', data);
  }
}
