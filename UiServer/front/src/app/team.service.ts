import { Injectable } from '@angular/core';
import {Http, Response, RequestOptions, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {HttpWrapperService} from "./http-wrapper.service";

@Injectable()
export class TeamService {

  constructor(private http: HttpWrapperService) { }

  fetchTeams(): Observable<Response> {
    return this.http.get('/api/team/show/all');
  }
}
