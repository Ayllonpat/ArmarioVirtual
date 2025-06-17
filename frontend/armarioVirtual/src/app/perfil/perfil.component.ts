import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';

import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [
    CommonModule,
    TopNavComponent,
    SideNavComponent,
    RouterModule
  ],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  perfil: any;
  viewType: 'conjuntos' | 'prendas' | 'favoritos' = 'conjuntos'; 

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.getPerfil().subscribe({
      next: (data) => {
        this.perfil = data;
      },
      error: (err) => console.error('Error cargando perfil:', err)
    });
  }

  select(type: 'conjuntos' | 'prendas' | 'favoritos') {
    this.viewType = type;
  }

  goToFollowers() {
    this.router.navigate(['/seguidores', this.perfil.id]);
  }

  goToFollowing() {
    this.router.navigate(['/seguidos', this.perfil.id]);
  }
}
