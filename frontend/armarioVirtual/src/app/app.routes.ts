import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { InicioComponent } from './inicio/inicio.component';
import { MiArmarioComponent } from './mi-armario/mi-armario.component';
import { PerfilComponent } from './perfil/perfil.component';
import { ExplorarComponent } from './explorar/explorar.component';
import { CrearConjuntoComponent } from './pages/crear-conjunto/crear-conjunto.component';
import { SubirPrendaComponent } from './pages/subir-prenda/subir-prenda.component';
import { DetallesPrendaComponent } from './detalles-prenda/detalles-prenda.component';
import { DetallesConjuntoComponent } from './detalles-conjunto/detalles-conjunto.component';
import { SeguidoresListComponent } from './seguidores/seguidores-list.component';
import { PerfilUsuarioComponent } from './perfil/perfil-usuario.component';
import { AdminTagsComponent } from './admin/admin-tags.component';

export const routes: Routes = [
  { path: '',     redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',    component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home',     component: HomeComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'explorar',   component: ExplorarComponent },
  { path: 'mi-armario', component: MiArmarioComponent },
  { path: 'perfil',     component: PerfilComponent },
  { path: 'mi-armario/crear-conjunto', component: CrearConjuntoComponent },
  { path: 'mi-armario/subir-prenda', component: SubirPrendaComponent },
  { path: 'prenda/:id', component: DetallesPrendaComponent },
  { path: 'conjunto/:id', component: DetallesConjuntoComponent },
  {
  path: 'seguidores/:id', component: SeguidoresListComponent },
{
  path: 'seguidos/:id',
  component: SeguidoresListComponent
},
{
  path: 'perfil/:id',component: PerfilUsuarioComponent
},
{ path: 'admin/tags', component: AdminTagsComponent },
  { path: '**',       redirectTo: 'login' }
];
