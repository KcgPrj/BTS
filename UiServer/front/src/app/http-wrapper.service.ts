import { Injectable } from '@angular/core';
import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs";

@Injectable()
export class HttpWrapperService {

  constructor(private http: Http) { }

  get(url: string): Observable<Response> {
    return this.http.get(url, this.createOption());
  }

  post(url: string, body: any): Observable<Response> {
    return this.http.post(url, body, this.createOption());
  }

  delete(url: string): Observable<Response> {
    return this.http.delete(url, this.createOption());
  }

  put(url: string, body: any): Observable<Response> {
    return this.http.put(url, body, this.createOption());
  }

  private createOption() {
    let headers = new Headers();
    var cookie = this.getCookie('access_token');
    console.log(cookie);
    headers.append('Authorization', `Bearer ${cookie}`);
    return {
      headers: headers
    };
  }

  private getCookie(name: string) {
    let ca: Array<string> = document.cookie.split(';');
    let caLen: number = ca.length;
    let cookieName = name + "=";
    let c: string;

    for (let i: number = 0; i < caLen; i += 1) {
      c = ca[i].replace(/^\s\+/g, "");
      if (c.indexOf(cookieName) == 0) {
        return c.substring(cookieName.length, c.length);
      }
    }
    throw new Error('認証情報が見つからない');
  }

}
