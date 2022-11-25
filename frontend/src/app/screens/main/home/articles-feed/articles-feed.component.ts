import { catchError, map, switchMap } from 'rxjs/operators';
import { ArticlesFeedService } from './../../../../services/articles/articles-feed.service';
import { ArticleFeedItem } from '@models/articles/article-feed-item.model';
import {
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
} from '@angular/core';
import { IPageInfo } from '@iharbeck/ngx-virtual-scroller';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'ua-articles-feed',
  templateUrl: './articles-feed.component.html',
  styleUrls: ['./articles-feed.component.scss'],
})
export class ArticlesFeedComponent {
  @Input() items: ArticleFeedItem[] = [];

  loading: boolean = false;
  loaded: boolean = false;

  constructor(
    private readonly articleFeedService: ArticlesFeedService
  ) {}

  protected fetchMore(event: IPageInfo) {
    if (!this.loaded && !this.loading) {
      console.log(`Calling for the new chunk ${JSON.stringify(event)}`);
      if (event.endIndex !== this.items.length - 1) return;
      this.loading = true;
      this.fetchNextChunk(this.items.length, 10)
        .pipe(
          switchMap((chunk) => {
            if(chunk.length == 0) {
              this.loaded = true;
            } else {
              this.items = this.items.concat(chunk);
            }
            this.loading = false;
            return of(!this.loaded);
          }),
          catchError((error) => {
            this.loading = false;
            console.log('Unable to load the next chunk' + error.error.message);
            return of(false);
          })
        )
        .subscribe((r) => console.log('Page updated: ' + r));
    }
  }

  fetchNextChunk(offset: number, limit: number): Observable<ArticleFeedItem[]> {
    return this.articleFeedService.feed(offset, limit);
  }
}
