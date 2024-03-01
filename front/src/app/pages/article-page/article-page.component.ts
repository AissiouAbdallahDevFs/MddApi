import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-article-page',
  templateUrl: './article-page.component.html',
  styleUrls: ['./article-page.component.scss']
})
export class ArticlePageComponent implements OnInit {
  articles: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchArticles();
  }

  redirectToCreateArticle(): void {
    this.router.navigate(['/article/add']);
  }

  fetchArticles(): void {
    this.http.get<any[]>('http://localhost:8080/api/articles')
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
}
