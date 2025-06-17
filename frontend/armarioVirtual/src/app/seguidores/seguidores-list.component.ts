import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-seguidores-list',
  standalone: true,
  imports: [
    CommonModule,      
    TopNavComponent,  
    SideNavComponent,
    RouterModule
  ],
  templateUrl: './seguidores-list.component.html',
  styleUrl: './seguidores-list.component.css'
})
export class SeguidoresListComponent implements OnInit {
  seguidores: any[] = [];
  seguidos: any[] = [];
  userId!: string;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id'];

    this.authService.getSeguidores(this.userId).subscribe({
      next: (res) => this.seguidores = res.content || res,
      error: (err) => console.error(err)
    });

    this.authService.getSeguidos(this.userId).subscribe({
      next: (res) => this.seguidos = res.content || res,
      error: (err) => console.error(err)
    });
  }
}
