import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  Inscription() {
    alert('btn Inscription clicked !');
  }
  Connexion() {
    alert('btn Connexion clicked !');
  }
}
