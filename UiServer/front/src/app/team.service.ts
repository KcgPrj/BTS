import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {HttpWrapperService} from "./http-wrapper.service";
import {Team} from "./team";
import {User} from "./user";
import {TeamCreateReq} from "./team-create-req";
import {JoinMemberReq} from "./join-member-req";
import {DefectionMemberReq} from "./defection-member-req";

@Injectable()
export class TeamService {

  constructor(private http: HttpWrapperService) {
  }

  fetchTeams(): Observable<Response> {
    return this.http.get('/api/team/show/all');
  }

  showTeam(teamId: string): Observable<Team> {
    return this.http.get('/api/show?teamId=' + teamId).map(res => res.json() as Team)
  }

  showTeamList(): Observable<Team[]> {
    return this.http.get('/api/team/show/all').map(res => res.json() as Team[])
  }

  showMember(teamId: string): Observable<User[]> {
    return this.http.get('/api/team/member/show?teamId=' + teamId).map(res => res.json() as User[])
  }

  createTeam(teamCreateReq: TeamCreateReq): Observable<Team> {
    return this.http.post('/api/team/create', teamCreateReq).map(res => res.json() as Team)
  }

  join(joinMemberReq: JoinMemberReq): Observable<Team> {
    return this.http.post("api/team/member/join", joinMemberReq).map(res => res.json() as Team)
  }

  defection(defectionMemberReq: DefectionMemberReq): Observable<Team> {
    return this.http.post("api/team/member/defection", defectionMemberReq).map(res => res.json() as Team)
  }
}
