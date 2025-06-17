import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TopNavComponent } from '../top-nav/top-nav.component';

@Component({
  selector: 'app-admin-tags',
  standalone: true,
  imports: [CommonModule, FormsModule, TopNavComponent],
  templateUrl: './admin-tags.component.html',
  styleUrls: ['./admin-tags.component.css']
})
export class AdminTagsComponent {
  nuevoTagNombre = '';
  mensaje = '';

  constructor(private http: HttpClient, private router: Router) {}

  crearTag() {
    if (!this.nuevoTagNombre.trim()) return;

    this.http.post('/api/tags/', { nombre: this.nuevoTagNombre }).subscribe({
      next: () => {
        this.mensaje = 'Tag creado con éxito!';
        this.nuevoTagNombre = '';
      },
      error: () => {
        this.mensaje = 'Error al crear tag.';
      }
    });
  }

  cerrarSesion() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
