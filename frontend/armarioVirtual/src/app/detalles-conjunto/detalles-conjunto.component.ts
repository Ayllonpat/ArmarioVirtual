import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';
import { ConjuntoService, DetalleConjunto } from '../services/conjunto.service';
import { ComentarioService, Comentario } from '../services/comentario.service';
import { AuthService } from '../services/auth.service'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detalles-conjunto',
  standalone: true,
  imports: [ CommonModule, TopNavComponent, SideNavComponent, FormsModule, RouterModule ],
  templateUrl: './detalles-conjunto.component.html',
  styleUrls: ['./detalles-conjunto.component.css']
})
export class DetallesConjuntoComponent implements OnInit {
  detalle?: DetalleConjunto;
  comentarios: Comentario[] = [];
  likes = 0;
  liked = false;
  nuevoComentario = '';
  usuarioLogueado = '';

  constructor(
    private route: ActivatedRoute,
    private srv: ConjuntoService,
    private comentarioSvc: ComentarioService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];

    this.srv.getDetalleConjunto(id).subscribe(d => {
      this.detalle = d;
      this.likes = d.likes;
    });

    this.comentarioSvc.getComentariosDeConjunto(id)
      .subscribe(cs => this.comentarios = cs);

      this.auth.getPerfil().subscribe(u => {
        this.usuarioLogueado = u.username;
      });
  }

  toggleLike() {
    if (!this.detalle) return;
    this.srv.toggleLike(this.detalle.conjunto.id).subscribe(() => {
      this.liked = !this.liked;
      this.likes += this.liked ? 1 : -1;
    });
  }

  enviarComentario() {
    if (!this.nuevoComentario.trim() || !this.detalle) return;

    this.comentarioSvc.crearComentarioEnConjunto(this.detalle.conjunto.id, {
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

  borrarConjunto() {
    if (!this.detalle) return;
    if (!confirm('¿Eliminar este conjunto?')) return;
    this.srv.deleteConjunto(this.detalle.conjunto.id).subscribe(() => {
      alert('Conjunto eliminado');
      this.router.navigateByUrl('/inicio');
    });
  }

  esPropietario() {
    return this.detalle?.conjunto.cliente.username === this.usuarioLogueado;
  }

  goToPrendaDetalle(prendaId: number) {
    this.router.navigateByUrl(`/prenda/${prendaId}`);
  }
}
