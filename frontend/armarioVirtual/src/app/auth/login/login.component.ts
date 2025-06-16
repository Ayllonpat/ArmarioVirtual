import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  showErrorModal = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit(): void {
    localStorage.removeItem('token');
    if (this.loginForm.invalid) return;

    this.auth.login(this.loginForm.value).subscribe({
      next: ({ token }) => {
        localStorage.setItem('token', token);
        this.router.navigate(['/inicio']);
      },
      error: () => {
        this.showErrorModal = true;
      }
    });
  }

  closeErrorModal(): void {
    this.showErrorModal = false;
  }
}
