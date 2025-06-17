import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PrendaService } from '../../services/prenda.service';
import { TipoPrendaService } from '../../services/tipo-prenda.service';
import { TagService } from '../../services/tag.service';
import { TopNavComponent } from '../../top-nav/top-nav.component';
import { SideNavComponent } from '../../side-nav/side-nav.component';

@Component({
  selector: 'app-subir-prenda',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    TopNavComponent,
    SideNavComponent
  ],
  templateUrl: './subir-prenda.component.html',
  styleUrls: ['./subir-prenda.component.css']
})
export class SubirPrendaComponent implements OnInit {
  prendaForm!: FormGroup;
  tipos: { id: number; nombre: string }[] = [];
  tags: { id: number; nombre: string }[] = [];
  selectedFile: File | null = null;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private prendaService: PrendaService,
    private tipoPrendaService: TipoPrendaService,
    private tagService: TagService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.prendaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(150)]],
      color: [''],
      talla: [''],
      enlaceCompra: ['', Validators.maxLength(255)],
      tipoPrendaId: [null, Validators.required],
      visibilidad: ['PRIVADO', Validators.required],
      tagIds: this.fb.array([], Validators.required)
    });

    // cargar tipos
    this.tipoPrendaService.getTipos().subscribe({
      next: data => this.tipos = data,
      error: () => this.error = 'No se pudieron cargar los tipos.'
    });

    // cargar tags
    this.tagService.getTags().subscribe({
      next: data => this.tags = data,
      error: () => this.error = 'No se pudieron cargar los tags.'
    });
  }

  get tagIdsArray() {
    return this.prendaForm.get('tagIds') as FormArray;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.selectedFile = input.files[0];
    }
  }

  onTagCheckChange(e: Event) {
    const checkbox = e.target as HTMLInputElement;
    const arr = this.tagIdsArray;
    const value = +checkbox.value;
    if (checkbox.checked) {
      arr.push(this.fb.control(value));
    } else {
      const idx = arr.controls.findIndex(ctrl => ctrl.value === value);
      if (idx > -1) arr.removeAt(idx);
    }
  }

  onSubmit(): void {
    if (this.prendaForm.invalid) {
      this.prendaForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.error = '';

    const dto = {
      nombre: this.prendaForm.value.nombre,
      color: this.prendaForm.value.color,
      talla: this.prendaForm.value.talla,
      enlaceCompra: this.prendaForm.value.enlaceCompra,
      visibilidad: this.prendaForm.value.visibilidad,
      imagen: 'placeholder.jpg',
      tipoPrenda: { id: this.prendaForm.value.tipoPrendaId },
      tagIds: this.prendaForm.value.tagIds  // <-- añadimos aquí
    };

    this.prendaService.createPrenda(dto).subscribe({
      next: prenda => {
        const prendaId = prenda.id;
        if (this.selectedFile) {
          this.prendaService.uploadImage(prendaId, this.selectedFile).subscribe({
            next: () => this.router.navigate(['/mi-armario']),
            error: () => {
              this.error = 'Error subiendo imagen';
              this.loading = false;
            }
          });
        } else {
          this.router.navigate(['/mi-armario']);
        }
      },
      error: () => {
        this.error = 'Error creando prenda';
        this.loading = false;
      }
    });
  }
}
