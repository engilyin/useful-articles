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
import { environment } from "@root/environments/environment";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";

import { TestBed } from "@angular/core/testing";

import { ArticlesFeedService } from "./articles-feed.service";
import { mockArticleFeedItemArray } from "@root/mocks/article-feed-mocks";

describe("ArticlesFeedService", () => {
  let service: ArticlesFeedService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    httpController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(ArticlesFeedService);
  });

  afterEach(() => {
    httpController.verify();
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("should call feed(0, 10) and return an array of ArticleFeedItem", () => {
    service.feed(0, 2).subscribe((res) => {
      expect(res).toEqual(mockArticleFeedItemArray);
    });

    const req = httpController.expectOne({
      method: "GET",
      url: `${environment.baseUrl}/api/articles?offset=0&limit=2`,
    });

    req.flush(mockArticleFeedItemArray);
  });
});
