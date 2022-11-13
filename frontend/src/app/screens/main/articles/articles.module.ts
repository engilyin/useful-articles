import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyArticlesComponent } from './my-articles/my-articles.component';
import { ArticleComponent } from './article/article.component';
import { ArticlesRoutingModule } from './articles-routing.module';



@NgModule({
  declarations: [
    MyArticlesComponent,
    ArticleComponent
  ],
  imports: [
    CommonModule,
    ArticlesRoutingModule
  ]
})
export class ArticlesModule { }
