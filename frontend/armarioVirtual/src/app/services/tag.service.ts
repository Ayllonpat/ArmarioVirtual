import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Tag {
  id: number;
  nombre: string;
}

@Injectable({ providedIn: 'root' })
export class TagService {
  constructor(private http: HttpClient) {}
  getTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>('/api/tags/');
  }
}
