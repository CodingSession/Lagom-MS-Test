import {Component, OnInit} from '@angular/core';
import {Http} from "@angular/http";
import "rxjs/add/operator/map";


@Component({
  selector: 'app-site',
  templateUrl: './site.component.html',
  styleUrls: ['./site.component.css']
})
export class SiteComponent implements OnInit {

  response: any = [0, 0, 0];

  constructor(private http: Http) {
  }

  ngOnInit() {

  }

  getJavaF() {
    return this.http.get('http://localhost:8080/java/functional')
      .map(res => (res.text()))
      .subscribe(respo => {
        this.response = respo;
      });
  }

  getJavaR() {
    return this.http.get('http://localhost:8080/java/reactive')
      .map(res => (res.text()))
      .subscribe(respo => {
        this.response = respo;
      });
  }
}
