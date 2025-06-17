import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TopNavComponent } from '../top-nav/top-nav.component';
import { SideNavComponent } from '../side-nav/side-nav.component';

import { PrendaService, Prenda } from '../services/prenda.service';
import { ConjuntoService, Conjunto } from '../services/conjunto.service';
import { TipoPrendaService, GetTipoPrendaDto } from '../services/tipo-prenda.service';
import { TagService, Tag } from '../services/tag.service';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-explorar',
  standalone: true,
  imports: [CommonModule, FormsModule, TopNavComponent, SideNavComponent, RouterModule],
  templateUrl: './explorar.component.html',
  styleUrl: './explorar.component.css'
})
export class ExplorarComponent implements OnInit {

  type: 'conjunto' | 'prenda' = 'conjunto';

  conjuntos: Conjunto[] = [];
  prendas: Prenda[] = [];

  filteredConjuntos: Conjunto[] = [];
  filteredPrendas: Prenda[] = [];

  tags: Tag[] = [];
  tiposPrenda: GetTipoPrendaDto[] = [];

  selectedTag: string = '';
  selectedTipoPrenda: string = '';

  constructor(
    private prendaService: PrendaService,
    private conjuntoService: ConjuntoService,
    private tipoPrendaService: TipoPrendaService,
    private tagService: TagService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.conjuntoService.getDetalleConjunto(0).subscribe(); 
    this.conjuntoService.getPrendas().subscribe(); 

    this.conjuntoService['http'].get<Conjunto[]>('/api/conjuntos').subscribe(res => {
      this.conjuntos = res.filter(c => c.visibilidad === 'PUBLICO');
      this.filteredConjuntos = this.conjuntos;
    });

    this.prendaService['http'].get<Prenda[]>('/api/prendas').subscribe(res => {
      this.prendas = res.filter(p => p.visibilidad === 'PUBLICO');
      this.filteredPrendas = this.prendas;
    });


    this.tipoPrendaService.getTipos().subscribe(res => {
      this.tiposPrenda = res;
    });

    this.tagService.getTags().subscribe(res => {
      this.tags = res;
    });
  }

  applyFilters() {
  if (this.type === 'conjunto') {
    this.filteredConjuntos = this.conjuntos.filter(c =>
      (!this.selectedTag || c.tags?.some(t => t.nombre === this.selectedTag))
    );
  } else {
    const tipoId = this.selectedTipoPrenda ? Number(this.selectedTipoPrenda) : null;

    this.filteredPrendas = this.prendas.filter(p =>
      (!this.selectedTag || p.tags?.some(t => t.nombre === this.selectedTag)) &&
      (!tipoId || Number(p.tipoPrendaId) === tipoId) 
    );
  }

}

goToDetalle(item: any) {
  const path = this.type === 'prenda'
    ? `prenda/${item.id}`
    : `conjunto/${item.id}`;
  this.router.navigateByUrl(path);
}


}
