import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticlesFeedComponent } from './articles-feed.component';

describe('ArticlesFeedComponent', () => {
  let component: ArticlesFeedComponent;
  let fixture: ComponentFixture<ArticlesFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticlesFeedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticlesFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
