import { Component, OnInit } from '@angular/core';
import {ReportService} from "../../domain/report/report.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Report} from "../../domain/report/report";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-report-details-page',
  templateUrl: './report-details-page.component.html',
  styleUrls: ['./report-details-page.component.scss']
})
export class ReportDetailsComponent implements OnInit {

  private productId: number;
  private teamId: string;
  private reportId: number;

  private report: Report;
  private subscription: Subscription;

  private titleToggle = false;
  private descToggle = false;
  private createdAtToggle = false;
  private assingToggle = false;
  private versionToggle = false;
  private stacktraceToggle = false;
  private logToggle = false;
  private runtimeToggle = false;
  private productToggle = false;
  private stateToggle = false;
  constructor(
    private reportService: ReportService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
      this.productId = params['productId'];
      this.reportId = params['reportId'];
    });
    this.updateReport();
  }

  updateReport() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.subscription = this.reportService.find(this.reportId).subscribe(it => this.report = it)
  }

  back() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }
}
