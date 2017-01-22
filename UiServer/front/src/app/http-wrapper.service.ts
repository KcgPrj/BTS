import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs";


@Injectable()
export class HttpWrapperService {

  private _host: string;
  private get host(): string {
    if (!this._host) {
      this._host = this.getCookie('host');
    }
    return this._host;
  }
  constructor(private http: Http) { }

  get(url: string): Observable<Response> {
    return this.http.get(this.host + url, this.createOption());
  }

  post(url: string, body: any): Observable<Response> {
    return this.http.post(this.host + url, body, this.createOption());
  }

  delete(url: string): Observable<Response> {
    return this.http.delete(this.host + url, this.createOption());
  }

  put(url: string, body: any): Observable<Response> {
    return this.http.put(this.host + url, body, this.createOption());
  }

  private createOption() {
    let headers = new Headers();
    var cookie = this.getCookie('access_token');
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
      c = ca[i].replace(/^\s+/g, "");
      if (c.indexOf(cookieName) == 0) {
        return c.substring(cookieName.length, c.length);
      }
    }
    throw new Error('認証情報が見つからない');
  }

}
