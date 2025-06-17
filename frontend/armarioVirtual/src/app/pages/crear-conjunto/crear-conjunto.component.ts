import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ConjuntoService } from '../../services/conjunto.service';
import { TagService, Tag } from '../../services/tag.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TopNavComponent } from '../../top-nav/top-nav.component';
import { SideNavComponent } from '../../side-nav/side-nav.component';

@Component({
  selector: 'app-crear-conjunto',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    TopNavComponent,
    SideNavComponent
  ],
  templateUrl: './crear-conjunto.component.html',
  styleUrls: ['./crear-conjunto.component.css']
})
export class CrearConjuntoComponent implements OnInit {
  form!: FormGroup;
  prendas: { id: number; nombre: string }[] = [];
  tags: Tag[] = [];
  selectedFile: File | null = null;
  loading = false;
  error = '';

  get prendasIds() { return this.form.get('prendasIds') as FormArray; }
  get tagsIds()    { return this.form.get('tagsIds') as FormArray;    }

  constructor(
    private fb: FormBuilder,
    private srv: ConjuntoService,
    private tagSrv: TagService,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre:     ['', [Validators.required, Validators.maxLength(150)]],
      prendasIds: this.fb.array([], Validators.required),
      tagsIds:    this.fb.array([], Validators.required),
      visibilidad:['PRIVADO', Validators.required]
    });

    this.srv.getPrendas().subscribe({
      next: data => this.prendas = data,
      error: () => this.error = 'No se pudieron cargar las prendas.'
    });

    this.tagSrv.getTags().subscribe({
      next: data => this.tags = data,
      error: () => this.error = 'No se pudieron cargar los tags.'
    });
  }

  onFileSelected(ev: Event) {
    const input = ev.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
    }
  }

  onCheckPrenda(e: Event) {
    const cb = e.target as HTMLInputElement;
    const val = +cb.value;
    if (cb.checked) this.prendasIds.push(this.fb.control(val));
    else {
      const idx = this.prendasIds.controls.findIndex(c => c.value === val);
      if (idx > -1) this.prendasIds.removeAt(idx);
    }
  }

  onCheckTag(e: Event) {
    const cb = e.target as HTMLInputElement;
    const val = +cb.value;
    if (cb.checked) this.tagsIds.push(this.fb.control(val));
    else {
      const idx = this.tagsIds.controls.findIndex(c => c.value === val);
      if (idx > -1) this.tagsIds.removeAt(idx);
    }
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.error = '';

    const dto = {
      nombre:      this.form.value.nombre,
      imagen:      'placeholder.jpg',
      prendasIds:  this.form.value.prendasIds,
      tagIds:      this.form.value.tagsIds,
      visibilidad: this.form.value.visibilidad
    };

    this.srv.createConjunto(dto).subscribe({
      next: conjunto => {
        const conjuntoId = conjunto.id;
        if (this.selectedFile) {
          this.srv.uploadImage(conjuntoId, this.selectedFile).subscribe({
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
        this.error = 'Error creando conjunto';
        this.loading = false;
      }
    });
  }
}
