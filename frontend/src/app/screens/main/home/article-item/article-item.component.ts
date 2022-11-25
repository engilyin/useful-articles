import { ArticleFeedItem } from '@models/articles/article-feed-item.model';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'ua-article-item',
  templateUrl: './article-item.component.html',
  styleUrls: ['./article-item.component.scss']
})
export class ArticleItemComponent implements OnInit {

  @Input() item?: ArticleFeedItem;

  constructor() { }

  ngOnInit(): void {
  }

}
