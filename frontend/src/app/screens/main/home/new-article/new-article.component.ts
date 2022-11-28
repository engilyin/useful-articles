import { catchError, filter, map, tap } from 'rxjs/operators';
import { ArticleFeedItem } from '@models/articles/article-feed-item.model';
import { ElementRef, EventEmitter, ViewChild } from '@angular/core';
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
import { pipe, takeUntil } from 'rxjs';
import { newArticleFormModel } from './new-article.form';
import { HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';

declare var window: any;

@Component({
  selector: 'ua-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss'],
})
export class NewArticleComponent extends ResourceHolder implements OnInit {

  @Output() onActicleCreated = new EventEmitter<ArticleFeedItem>();

  @ViewChild('articleAttachment') attachControl!: ElementRef;

  newArticleForm = newArticleFormModel(this.formBuilder);

  formModal: any;

  //newArticleErrorMessage$?: Observable<string>;

  posting = false;

  percentDone = 0;

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
        .add(this.newArticleForm.value, this.attachControl.nativeElement.files?.item(0))
        .pipe(takeUntil(this.destroy$))
        .subscribe((event: HttpEvent<any>) => {
          switch (event.type) {
            case HttpEventType.Sent:
              console.log('Request has been made!');
              break;
            case HttpEventType.ResponseHeader:
              console.log('Response header has been received!');
              break;
            case HttpEventType.UploadProgress:
              this.percentDone = Math.round(event.loaded / event.total! * 100);
              console.log(`Uploaded! ${this.percentDone}%`);
              break;
            case HttpEventType.Response:
              console.log('User successfully created!', event.body);
              const newArticleResult = event.body;
              this.postedNewArticle(newArticleResult)
              this.percentDone = 0;
          }
        })
        // .pipe(
        //   this.uploadProgress((progress) => (this.percentDone = progress)),
        //   this.toResponseBody()
        // )
        // .subscribe({
        //   next: (newArticleResult) => this.postedNewArticle(newArticleResult as ArticleFeedItem),
        //   error: (msg) => this.handlePostError(msg),
        // });
    } else {
      this.errorMessage = 'Please enter valid data!';
    }
    this.cd.detectChanges();
  }

  uploadProgress<T>(cb: (progress: number) => void) {
    return tap((event: HttpEvent<T>) => {
      if (event.type === HttpEventType.UploadProgress) {
        cb(Math.round((100 * event.loaded) / event.total!));
      }
    });
  }

  toResponseBody<T>() {
    return pipe(
      filter(( event: HttpEvent<T> ) => event.type === HttpEventType.Response),
      map((event: HttpEvent<T> ) => (event as HttpResponse<T>).body)
    );
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
