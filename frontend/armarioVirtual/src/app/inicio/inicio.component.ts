import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';
import {jwtDecode} from 'jwt-decode';

interface Item {
  id: number;
  nombre: string;
  imagenUrl: string;
  visibilidad: 'PUBLICO' | 'PRIVADO'; 
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
  const token = localStorage.getItem('token');
  if (token) {
    const decoded: any = jwtDecode(token);
    const role = decoded?.role || '';
    const authorities: string[] = decoded?.authorities || [];

    if (authorities.includes('ROLE_ADMIN') || role === 'ADMIN' || role === 'ROLE_ADMIN') {
      this.router.navigate(['/admin/tags']);
      return;
    }
  }

  this.http.get<Item[]>('/api/conjuntos').subscribe(data => {
    this.conjuntos = data.filter(c => c.visibilidad === 'PUBLICO');
  });
  this.http.get<Item[]>('/api/prendas').subscribe(data => {
    this.prendas = data.filter(p => p.visibilidad === 'PUBLICO');
  });
}

  select(type: 'conjuntos' | 'prendas') {
    this.viewType = type;
  }

  goToDetalle(item: Item) {
    const path = this.viewType === 'prendas'
      ? `prenda/${item.id}`
      : `conjunto/${item.id}`;
    this.router.navigateByUrl(path);
  }
}
