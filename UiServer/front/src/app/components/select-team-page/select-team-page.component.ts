import { Component, OnInit } from '@angular/core';
import {TeamService} from "../../domain/team/team.service";
import {Team} from "../../domain/team/team";

@Component({
  selector: 'app-select-team-page',
  templateUrl: './select-team-page.component.html',
  styleUrls: ['./select-team-page.component.css']
})
export class SelectTeamPageComponent implements OnInit {

  private teams: Team[] = [];
  constructor(private teamService: TeamService) { }

  ngOnInit() {
    this.teamService.fetchTeams().subscribe(it => this.teams = it);
  }

  createTeam() {
    const teamId = 'team' + Math.floor(Math.random() * 10000);
    this.teamService.createTeam(teamId, teamId).subscribe(it => this.teams.push(it));
  }

}
