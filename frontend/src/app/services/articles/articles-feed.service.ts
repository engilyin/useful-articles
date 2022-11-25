import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ArticleFeedItem } from '@models/articles/article-feed-item.model';
import { environment } from '@root/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArticlesFeedService {

  constructor(private httpClient: HttpClient) {}

  public feed(offset: number, limit: number): Observable<ArticleFeedItem[]> {

    const requestParams = { offset: `${offset}`, limit: `${limit}` };

    return this.httpClient.get<[]>(`${environment.baseUrl}/api/articles`, { params: requestParams });
  }

}
