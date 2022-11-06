import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth/auth.guard';
import { SigninComponent } from './screens/auth/signin/signin.component';
import { TestComponent } from './screens/test/test/test.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/home'
  },

  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      {
        path: 'home',
        component: TestComponent,
      },
      // {
      //   path: AppRoutes.Search,
      //   loadChildren: () => import('@pages/search/search.module').then((m) => m.SearchModule)
      // }
    ]
  },
  {
    path: 'signin',
    component: SigninComponent
  },
  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
