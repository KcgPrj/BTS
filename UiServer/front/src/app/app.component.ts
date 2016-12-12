import { Component } from '@angular/core';
import {TeamService} from "./team.service";
import {Response} from "@angular/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app works!';


  constructor(teamService: TeamService) {
    teamService.fetchTeams().subscribe(res => this.hoge(res), e => this.title = e);
  }

  hoge(res: Response) {
    this.title = res.toString();
    console.log(res);
  }
}
