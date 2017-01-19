import { Component, OnInit } from '@angular/core';
import {TeamService} from "../../domain/team/team.service";
import {Team} from "../../domain/team/team";
import {ModalService} from "../../modal.service";

@Component({
  selector: 'app-select-team-page',
  templateUrl: './select-team-page.component.html',
  styleUrls: ['./select-team-page.component.css'],
  providers: [ModalService]
})
export class SelectTeamPageComponent implements OnInit {

  private teams: Team[] = [];

  private teamForm: Team;
  constructor(
    private modalService: ModalService,
    private teamService: TeamService) { }

  ngOnInit() {
    this.clearTeamForm();
    this.teamService.fetchTeams().subscribe(it => this.teams = it);
  }

  clearTeamForm() {
    this.teamForm = new Team("", "", null, [], []);
  }

  createTeam() {
    this.teamService.createTeam(this.teamForm.teamId, this.teamForm.teamName)
      .subscribe(it => {
        this.teams.push(it);
      });
    this.clearTeamForm();
    this.modalService.closeTeamModal();
  }

}
