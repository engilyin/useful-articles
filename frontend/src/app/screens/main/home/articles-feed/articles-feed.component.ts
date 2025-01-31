/*
 Copyright 2022-2025 engilyin

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
import { catchError, map, switchMap } from "rxjs/operators";
import { ArticlesFeedService } from "./../../../../services/articles/articles-feed.service";
import { ArticleFeedItem } from "@models/articles/article-feed-item.model";
import { ChangeDetectorRef, Component, Input, ViewChild } from "@angular/core";
import {
  IPageInfo,
  VirtualScrollerComponent,
} from "@iharbeck/ngx-virtual-scroller";
import { Observable, of } from "rxjs";

@Component({
  selector: "ua-articles-feed",
  templateUrl: "./articles-feed.component.html",
  styleUrls: ["./articles-feed.component.scss"],
})
export class ArticlesFeedComponent {
  @Input() items: ArticleFeedItem[] = [];

  @ViewChild(VirtualScrollerComponent) scroller?: VirtualScrollerComponent;

  loading: boolean = false;
  loaded: boolean = false;

  constructor(
    private readonly articleFeedService: ArticlesFeedService,
    private readonly cd: ChangeDetectorRef
  ) {}

  protected fetchMore(event: IPageInfo) {
    if (!this.loaded && !this.loading) {
      //console.log(`Calling for the new chunk ${JSON.stringify(event)}`);
      if (event.endIndex !== this.items.length - 1) {
        return;
      }

      this.loading = true;
      const firstItemId: number | undefined =
        this.items.length > 0 ? this.items[0].articleId : undefined;
      this.fetchNextChunk(this.items.length, 10, firstItemId)
        .pipe(
          switchMap((chunk) => {
            if (chunk.length == 0) {
              this.loaded = true;
            } else {
              this.items = this.items.concat(chunk);
            }
            this.loading = false;
            return of(!this.loaded);
          }),
          catchError((error) => {
            this.loading = false;
            console.log("Unable to load the next chunk" + error.error.message);
            return of(false);
          })
        )
        .subscribe((r) => console.log("Page updated: " + r));
    }
  }

  fetchNextChunk(
    offset: number,
    limit: number,
    firstItemId?: number
  ): Observable<ArticleFeedItem[]> {
    return this.articleFeedService.feed(offset, limit, firstItemId);
  }

  public newArticleAdded(newArticle: ArticleFeedItem) {
    console.log(
      `Home feed see the new message and trying to update the feed with ${JSON.stringify(
        newArticle
      )}`
    );
    this.items = [];
    this.scroller!.refresh();
  }
}
