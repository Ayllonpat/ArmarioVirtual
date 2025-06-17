import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';

import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';

import { AuthService } from '../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-perfil-usuario',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    TopNavComponent,
    SideNavComponent,
    
  ],
  templateUrl: './perfil-usuario.component.html',
  styleUrl: './perfil-usuario.component.css'
})
export class PerfilUsuarioComponent implements OnInit {

  usuario: any;
  prendas: any[] = [];
  conjuntos: any[] = [];
  isFollowing: boolean = false;
  userId!: string;

    viewType: 'conjuntos' | 'prendas' = 'conjuntos';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id'];

    this.authService.getPerfil().subscribe({
      
      next: me => {
        // ✅ 2) Si el id es el mismo, redirigimos al perfil privado
        if (me.id === this.userId) {
          this.router.navigate(['/perfil']);
          return;
        }
        // ✅ 3) Si no, seguimos cargando el perfil público
        this.loadProfile();
        this.loadPrendas();
        this.loadConjuntos();
        this.checkFollowing(me);
      },
      error: err => console.error(err)
    });
  }

  loadProfile() {
    this.http.get<any>(`/api/usuarios/${this.userId}`).subscribe({
      next: res => this.usuario = res,
      error: err => console.error(err)
    });
  }

  loadPrendas() {
    this.http.get<any[]>(`/api/prendas/usuario/${this.userId}`).subscribe({
      next: res => this.prendas = res.filter(p => p.visibilidad === 'PUBLICO'),
      error: err => console.error(err)
    });
  }

  loadConjuntos() {
    this.http.get<any[]>(`/api/conjuntos/usuario/${this.userId}`).subscribe({
      next: res => this.conjuntos = res.filter(c => c.visibilidad === 'PUBLICO'),
      error: err => console.error(err)
    });
  }

  checkFollowing(me: any) {
  this.isFollowing = me.seguidos?.some((s: any) => s.id === this.userId) || false;
}


  toggleFollow() {
    const endpoint = this.isFollowing ? `/api/usuarios/${this.userId}/unfollow` : `/api/usuarios/${this.userId}/follow`;
    this.http.post(endpoint, {}).subscribe({
      next: () => {
        this.isFollowing = !this.isFollowing;
      },
      error: err => console.error(err)
    });
  }

  goToFollowers() {
    this.router.navigate(['/seguidores', this.userId]);
  }

  goToFollowing() {
    this.router.navigate(['/seguidos', this.userId]);
  }

}
