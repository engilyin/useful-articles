import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleComponent } from './article/article.component';
import { MyArticlesComponent } from './my-articles/my-articles.component';



const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: MyArticlesComponent
      },
      {
        path: 'article',
        component: ArticleComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ArticlesRoutingModule { }
