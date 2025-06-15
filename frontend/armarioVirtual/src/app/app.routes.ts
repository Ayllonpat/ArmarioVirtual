import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { InicioComponent } from './inicio/inicio.component';

export const routes: Routes = [
  { path: '',     redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',    component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home',     component: HomeComponent },
  { path: 'inicio', component: InicioComponent },
  { path: '**',       redirectTo: 'login' }
];
