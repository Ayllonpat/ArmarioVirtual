import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  showSuccessModal = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      nombre:   ['', Validators.required],      
      username: ['', Validators.required],
      email:    ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }  

  onSubmit(): void {
    if (this.registerForm.invalid) return;
    this.auth.register(this.registerForm.value).subscribe({
      next: () => {
        // en lugar de navegar, muestro el modal
        this.showSuccessModal = true;
      },
      error: err => console.error(err)
    });
  }

  closeModal(): void {
    this.showSuccessModal = false;
    this.router.navigate(['/login']);
  }
}
