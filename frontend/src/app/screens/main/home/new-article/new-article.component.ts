import { catchError } from 'rxjs/operators';
import { ArticleFeedItem } from '@models/articles/article-feed-item.model';
import { EventEmitter } from '@angular/core';
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

import { ChangeDetectorRef, Component, OnInit, Output } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { ResourceHolder } from '@root/app/components/sys/resource-holder';
import { AddArticleService } from '@root/app/services/articles/add-article.service';
import { takeUntil } from 'rxjs';
import { newArticleFormModel } from './new-article.form';

declare var window: any;

@Component({
  selector: 'ua-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss'],
})
export class NewArticleComponent extends ResourceHolder implements OnInit {
  @Output() onActicleCreated = new EventEmitter<ArticleFeedItem>();

  newArticleForm = newArticleFormModel(this.formBuilder);

  formModal: any;

  //newArticleErrorMessage$?: Observable<string>;

  posting = false;

  errorMessage = '';

  constructor(
    private readonly addArticleService: AddArticleService,
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly cd: ChangeDetectorRef
  ) {
    super();
  }
  ngOnInit(): void {
    // this.newArticleErrorMessage$ = ???;
    this.formModal = new window.bootstrap.Modal(
      document.getElementById('addNewArticle')
    );
  }

  post() {
    console.log('post new article');
    if (this.newArticleForm.valid) {
      this.formModal.hide();
      this.posting = true;
      this.addArticleService
        .add(this.newArticleForm.value)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (newArticleResult) => this.postedNewArticle(newArticleResult),
          error: (msg) => this.handlePostError(msg),
        });
    } else {
      this.errorMessage = 'Please enter valid data!';
    }
    this.cd.detectChanges();
  }

  postedNewArticle(newArticleResult: ArticleFeedItem): void {
    this.posting = false;
    this.onActicleCreated.emit(newArticleResult);
    this.cd.detectChanges();
  }

  handlePostError(errorMessage: string) {
    this.errorMessage = errorMessage;
    this.formModal.show();
  }
}

