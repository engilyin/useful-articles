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
import { mockArticleFeedItem1 } from "./../../../mocks/article-feed-mocks";
import { NewArticleRequest } from "./../../models/articles/new-article-request.model";
import { TestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";

import { AddArticleService } from "./add-article.service";
import { environment } from "@root/environments/environment";

describe("AddArticleService", () => {
  let service: AddArticleService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    httpController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AddArticleService);
  });

  afterEach(() => {
    httpController.verify();
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("should call add(newArticle) and return the new ArticleFeedItem", () => {
    const newArticleRequest: NewArticleRequest = {
      articleName: mockArticleFeedItem1.articleName,
      articleDescription: mockArticleFeedItem1.articleDescription,
      articleAttachment: mockArticleFeedItem1.articleAttachment,
    };

    service.add(newArticleRequest).subscribe((res) => {
      expect(res).toEqual(mockArticleFeedItem1);
    });

    const req = httpController.expectOne({
      method: "POST",
      url: `${environment.baseUrl}/api/articles`,
    });

    req.flush(mockArticleFeedItem1);
  });
});
