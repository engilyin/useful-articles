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
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { HomeRoutingModule } from './home-routing.module';
import { VirtualScrollerModule } from '@iharbeck/ngx-virtual-scroller';
import { NewArticleComponent } from './new-article/new-article.component';
import { ArticlesFeedComponent } from './articles-feed/articles-feed.component';
import { ArticleItemComponent } from './article-item/article-item.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PlayerComponent } from './player/player.component';



@NgModule({
  declarations: [
    HomeComponent,
    NewArticleComponent,
    ArticlesFeedComponent,
    ArticleItemComponent,
    PlayerComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    ReactiveFormsModule,
    VirtualScrollerModule
  ]
})
export class HomeModule { }
