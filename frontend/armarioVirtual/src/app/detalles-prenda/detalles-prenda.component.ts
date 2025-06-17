import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';
import { ComentarioService, Comentario } from '../services/comentario.service';
import { AuthService } from '../services/auth.service';
import { PrendaService, Prenda } from '../services/prenda.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detalles-prenda',
  standalone: true,
  imports: [CommonModule, TopNavComponent, SideNavComponent, FormsModule, RouterModule],
  templateUrl: './detalles-prenda.component.html',
  styleUrls: ['./detalles-prenda.component.css'],
})
export class DetallesPrendaComponent implements OnInit {
  prenda?: Prenda;
  comentarios: Comentario[] = [];
  nuevoComentario = '';
  usuarioLogueado = '';
  likes = 0;
  liked = false;

  constructor(
    private route: ActivatedRoute,
    private prendaSvc: PrendaService,
    private comentarioSvc: ComentarioService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];

    this.prendaSvc.getById(id).subscribe(p => {
      this.prenda = p;
    });

    this.prendaSvc.getLikeCount(id).subscribe(count => {
      console.log(count)
      this.likes = count.likeCount;
    });

    this.comentarioSvc.getComentariosDePrenda(id)
      .subscribe(cs => this.comentarios = cs);

    this.auth.getPerfil().subscribe(u => {
      this.usuarioLogueado = u.username;
    });
  }

  toggleLike() {
    const prenda = this.prenda;
    if (!prenda) return;
  
    this.prendaSvc.toggleLike(prenda.id).subscribe(() => {
      this.prendaSvc.getLikeCount(prenda.id).subscribe(count => {
        this.likes = count.likeCount;
      });
      this.liked = !this.liked;
    });
  }
  

  enviarComentario() {
    if (!this.nuevoComentario.trim() || !this.prenda) return;

    this.comentarioSvc.crearComentarioEnPrenda(this.prenda.id, {
      contenido: this.nuevoComentario
    }).subscribe(c => {
      this.comentarios.push(c);
      this.nuevoComentario = '';
    });
  }

  borrarComentario(id: number) {
    if (!confirm('¿Eliminar este comentario?')) return;
    this.comentarioSvc.eliminarComentario(id).subscribe(() => {
      this.comentarios = this.comentarios.filter(c => c.id !== id);
    });
  }

  borrarPrenda() {
    if (!this.prenda) return;
    if (!confirm('¿Eliminar esta prenda?')) return;
  
    this.prendaSvc.delete(this.prenda.id).subscribe({
      next: () => {
        alert('Prenda eliminada');
        this.router.navigateByUrl('/inicio');
      },
      error: (err) => {
        const mensaje = 'No se pudo eliminar la prenda porque se encuentra en un conjunto';
        alert(mensaje);
      }
    });
  }
  

  esPropietario() {
    return this.prenda?.cliente?.username === this.usuarioLogueado;
  }
}
