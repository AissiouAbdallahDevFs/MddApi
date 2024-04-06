import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ArticlePage } from 'src/app/interfaces/article.interface';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-article-page',
  templateUrl: './article-page.component.html',
  styleUrls: ['./article-page.component.scss']
})
export class ArticlePageComponent implements OnInit, OnDestroy {
  articles: ArticlePage[] = [];
  private articlesSubscription: Subscription | undefined;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchArticles();
  }

  ngOnDestroy(): void {
    if (this.articlesSubscription) {
      this.articlesSubscription.unsubscribe();
    }
  }

  redirectToCreateArticle(): void {
    this.router.navigate(['/article/add']);
  }

  fetchArticles(): void {
    this.articlesSubscription = this.http.get<ArticlePage[]>('/api/articles')
      .subscribe(
        (response) => {
          this.articles = response;
        },
        (error) => {
          console.error('Erreur lors de la récupération des articles :', error);
        }
      );
  }

  redirectToArticleDetail(id: number): void {
    this.router.navigate(['/article', id]);
  }

  sortArticlesAZ(): void {
    this.articles.sort((a, b) => a.title.localeCompare(b.title));
  }

  sortArticlesZA(): void {
    this.articles.sort((a, b) => b.title.localeCompare(a.title));
  }
}
