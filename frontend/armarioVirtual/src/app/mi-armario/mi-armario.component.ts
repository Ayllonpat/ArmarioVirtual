import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';

interface Item {
  id: number;
  nombre: string;
  imagenUrl: string;
}

@Component({
  selector: 'app-mi-armario',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    RouterModule,
    TopNavComponent,
    SideNavComponent
  ],
  templateUrl: './mi-armario.component.html',
  styleUrls: ['./mi-armario.component.css']
})
export class MiArmarioComponent implements OnInit {
  viewType: 'conjuntos' | 'prendas' = 'conjuntos';
  items: Item[] = [];

  constructor(private http: HttpClient, private router: Router) {
 
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.loadItems();
      });
  }

  ngOnInit(): void {
    this.loadItems();
  }

  select(type: 'conjuntos' | 'prendas'): void {
    this.viewType = type;
    this.loadItems();
  }

  loadItems(): void {
    const url = this.viewType === 'conjuntos'
      ? '/api/conjuntos/cliente'
      : '/api/prendas/cliente';

    this.http.get<Item[]>(url).subscribe(data => this.items = data);
  }

  goToDetalle(item: Item): void {
    const path = this.viewType === 'prendas'
      ? `/prenda/${item.id}`
      : `/conjunto/${item.id}`;
    this.router.navigateByUrl(path);
  }

  navigateToForm(): void {
    const path = this.viewType === 'conjuntos'
      ? '/mi-armario/crear-conjunto'
      : '/mi-armario/subir-prenda';
    this.router.navigate([path]);
  }
}
