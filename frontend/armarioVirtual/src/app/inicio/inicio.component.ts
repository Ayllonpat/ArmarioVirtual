import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
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
  imports: [ CommonModule, RouterModule, TopNavComponent, SideNavComponent ],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  viewType: 'conjuntos' | 'prendas' = 'conjuntos';
  conjuntos: Item[] = [];
  prendas: Item[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.http.get<Item[]>('/api/conjuntos').subscribe(data => this.conjuntos = data);
    this.http.get<Item[]>('/api/prendas').subscribe(data => this.prendas = data);
  }

  select(type: 'conjuntos' | 'prendas') {
    this.viewType = type;
  }

  goToDetalle(item: Item) {
    const path = this.viewType === 'prendas' ? `prenda/${item.id}` : `conjunto/${item.id}`;
    this.router.navigateByUrl(path);
  }
}
