import { environment } from '@root/environments/environment';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TestBed } from '@angular/core/testing';

import { ArticlesFeedService } from './articles-feed.service';
import { mackArticleFeedItemArray } from '@root/mocks/article-feed-mocks';

describe('ArticlesFeedService', () => {
  let service: ArticlesFeedService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ArticlesFeedService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call feed(0, 10) and return an array of ArticleFeedItem', () => {

    // 1
    service.feed(0, 2).subscribe((res) => {
      //2
      expect(res).toEqual(mackArticleFeedItemArray);
    });

    //3
    const req = httpController.expectOne({
      method: 'GET',
      url: `${environment.baseUrl}/api/articles?offset=0&limit=2`,
    });

    //4
    req.flush(mackArticleFeedItemArray);
  });
});
