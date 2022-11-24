export interface ArticleFeedItem {
  readonly articleId: number;
  readonly articleName: string;
  readonly articleDescription: string;
  readonly articleAttachment: string;
  readonly articleCreated: number;
  readonly authorUsername: string;
  readonly authorFullname: string;
  readonly commentCount: number;
}
