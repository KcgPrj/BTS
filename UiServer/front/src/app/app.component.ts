import {Component} from '@angular/core';
import {TeamService} from "./team.service";
import {Response} from "@angular/http";
import {ReportService} from "./report.service";
import {ProductService} from "./product.service";
import {CreateTeamReq} from "./create-team-req";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app works!';


  constructor(teamService: TeamService, reportService: ReportService, productService: ProductService) {
  //  teamService.fetchTeams().subscribe(res => this.hoge(res), e => this.title = e);
    teamService.createTeam(new CreateTeamReq("team","team"));
    //teamService.showTeamList().subscribe(res => this.title = "success", e => this.title = "hoge")


  }

  hoge(res: Response) {
    this.title = res.toString();

    console.log(res);
  }
}
