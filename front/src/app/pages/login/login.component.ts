import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  formData: any = {}; 

  constructor(private http: HttpClient,private router: Router, private _snackBar: MatSnackBar ) {}

  ngOnInit(): void {}

 login() {
    this.http.post<any>('/api/auth/login', this.formData)
      .subscribe(
        (response) => {
          if (response && response.token) {
            console.log('Connexion réussie ! Token:', response.token);
            localStorage.setItem('token', response.token);
            this.router.navigate(['/article']);
          } 
        }, 
        (error) => {
          console.error('Erreur lors de la connexion:', error);
          this._snackBar.open('Erreur lors de la connexion', 'Fermer', {
            duration: 500,
          });

        }
      );
  }
}
