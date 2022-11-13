/*
 Copyright 2022 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth/auth.guard';
import { SigninComponent } from './screens/auth/signin/signin.component';
import { MainComponent } from './screens/main/_node/main.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/home'
  },

  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'home',
        loadChildren: () => import('@screens/main/home/home.module').then((m) => m.HomeModule)
      },
      {
        path: 'users',
        loadChildren: () => import('@screens/main/users/users.module').then((m) => m.UsersModule)
      },
      {
        path: 'articles',
        loadChildren: () => import('@screens/main/articles/articles.module').then((m) => m.ArticlesModule)
      },
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
