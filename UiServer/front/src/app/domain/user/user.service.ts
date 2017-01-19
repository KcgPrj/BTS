import { Injectable } from '@angular/core';
import {HttpWrapperService} from "../../http-wrapper.service";
import {Observable} from "rxjs";
import {User} from "./user";

@Injectable()
export class UserService {

  constructor(private http: HttpWrapperService) {
  }

  fetchAll(): Observable<User[]> {
    return this.http.get('/api/user/all').map(res => res.json() as User[])
  }
}
