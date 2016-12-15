import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpWrapperService} from "../../http-wrapper.service";
import {Team} from "./team";
import {User} from "../user/user";
import 'rxjs/add/operator/map';

@Injectable()
export class TeamService {

  constructor(private http: HttpWrapperService) {
  }

  fetchTeams(): Observable<Team[]> {
    return this.http.get('/api/team/show/all').map(res => res.json() as Team[]);
  }

  showTeam(teamId: string): Observable<Team> {
    return this.http.get('/api/show?teamId=' + teamId).map(res => res.json() as Team)
  }

  showTeamList(): Observable<Team[]> {
    return this.http.get('/api/team/show/all').map(res => res.json() as Team[])
  }

  showMember(teamId: string): Observable<User[]> {
    return this.http.get(`/api/team/member/show?teamId=${teamId}`).map(res => res.json() as User[])
  }

  createTeam(teamId: string, teamName: string): Observable<Team> {
    return this.http.post('/api/team/create', {teamId: teamId, teamName: teamName}).map(res => res.json() as Team)
  }

  join(teamId: string, userId: number): Observable<Team> {
    return this.http.post("/api/team/member/join", {teamId: teamId, userId: userId}).map(res => res.json() as Team)
  }

  defection(teamId: string, userId: number): Observable<Team> {
    return this.http.post("/api/team/member/defection", {teamId: teamId, userId: userId}).map(res => res.json() as Team)
  }
}
