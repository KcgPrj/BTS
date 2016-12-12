import {Component} from '@angular/core';
import {TeamService} from "./team.service";
import {Response} from "@angular/http";
import {ReportService} from "./report.service";
import {ProductService} from "./product.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app works!';


  constructor(teamService: TeamService, reportService: ReportService, productService: ProductService) {
    teamService.fetchTeams().subscribe(res => this.hoge(res), e => this.title = e);
  }

  hoge(res: Response) {
    this.title = res.toString();

    console.log(res);
  }
}
