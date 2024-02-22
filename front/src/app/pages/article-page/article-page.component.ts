// article-page.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-article-page',
  templateUrl: './article-page.component.html',
  styleUrls: ['./article-page.component.scss']
})
export class ArticlePageComponent implements OnInit {
  articles: any[] = [];
  indexArray: number[] = [];

  constructor(@Inject(HttpClient) private http: HttpClient, @Inject(Router) private router: Router) {}

  ngOnInit(): void {
    this.fetchArticles();
  }
  
  redirectToCreateArticle() {
    this.router.navigate(['/article/add']);
  }

  fetchArticles(): void {
    this.http.get<any>('http://localhost:8080/api/articles')
      .subscribe((response) => {
        this.articles = response.articles;
        for (let i = 0; i < this.articles.length / 2; i++) {
          this.indexArray.push(i);
        }
      }, (error) => {
        console.error('Erreur lors de la récupération des articles :', error);
      });
  }
}
