import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private token: string | null = localStorage.getItem('token');

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials)
      .pipe(
        map(response => {
          if (response && response.token) {
            this.setToken(response.token);
          
          }
          return response;
        })
      );
  }

  setToken(token: string | null): void {
    this.token = token;
    localStorage.setItem('token', token as string);
  }

  getToken(): string | null {
    return this.token;
  }
  logout(): void {
    localStorage.removeItem('token');
  }

  getHeaders(): HttpHeaders {
    const headersConfig: { [key: string]: string } = {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    };

    if (this.token) {
      headersConfig['Authorization'] = `Bearer ${this.token}`;
      headersConfig['Access-Control-Allow-Origin'] = '*';
    }

    return new HttpHeaders(headersConfig);
  }
}

