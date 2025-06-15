import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';

interface Item {
  id: number;
  nombre: string;
  imagenUrl: string;
}

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    TopNavComponent,
    SideNavComponent
  ],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  viewType: 'conjuntos' | 'prendas' = 'conjuntos';
  conjuntos: Item[] = [];
  prendas: Item[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Item[]>('/api/conjuntos')
      .subscribe(data => this.conjuntos = data);
    this.http.get<Item[]>('/api/prendas')
      .subscribe(data => this.prendas = data);
  }

  select(type: 'conjuntos' | 'prendas'): void {
    this.viewType = type;
  }
}
