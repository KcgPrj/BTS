import {Component} from '@angular/core';
import {TeamService} from "./domain/team/team.service";
import {Response} from "@angular/http";
import {ReportService} from "./domain/report/report.service";
import {ProductService} from "./domain/product/product.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor() {
  }
}
