import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';


  response: any = [0, 0, 0];
  time: any = 0;

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.response.min = 0;
    this.response.max = 0;
    this.response.avg = 0;

  }

  getJavaF() {
    var start = new Date().getTime();
    return this.http.get('http://localhost:8080/java/functional')
      .subscribe(respo => {
        this.response = respo;
        this.time = new Date().getTime() - start;
      });
  }

  getJavaR() {
    var start = new Date().getTime();

    return this.http.get('http://localhost:8080/java/reactive')
      .subscribe(respo => {
        this.response = respo;
        this.time = new Date().getTime() - start;

      });
  }


  getJavaMW() {
    var start = new Date().getTime();

    return this.http.get('http://localhost:8080/java/moving')
      .subscribe(respo => {
        this.response = respo;
        this.time = new Date().getTime() - start;

      });
  }

  getScala() {
    var start = new Date().getTime();

    return this.http.get('http://localhost:9000/scala/functional')
      .subscribe(respo => {
        this.response = respo;
        this.time = new Date().getTime() - start;

      });
  }
}


