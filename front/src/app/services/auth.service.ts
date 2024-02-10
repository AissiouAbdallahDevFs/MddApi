import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private token: string | null = null;

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials);
  }

  setToken(token: string | null): void {
    this.token = token;
  }

  getToken(): string | null {
    return this.token;
  }

getHeaders(): HttpHeaders {
    const headersConfig: { [key: string]: string } = {
        'Content-Type': 'application/json',
        Accept: 'application/json'
    };

    if (this.token) {
        headersConfig['Authorization'] = `Bearer ${this.token}`;
    }

    return new HttpHeaders(headersConfig);
}
}
