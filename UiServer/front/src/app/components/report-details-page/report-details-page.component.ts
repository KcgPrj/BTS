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
    this.reloadReport();
  }

  reloadReport() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.subscription = this.reportService.find(this.reportId
    ).subscribe(it => {
      this.report = it;
      if (this.report.state === 'opened') {
        this.stateToggle = true;
      } else {
        this.stateToggle = false;
      }
    });
  }

  back() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  toggleDescEdit() {
    this.descToggle = !this.descToggle;
  }

  toggleTitleEdit() {
    this.titleToggle = !this.titleToggle;
  }

  onStateChanged() {
    this.report.state = this.stateToggle ? 'opened' : 'closed';
    console.log(this.stateToggle);
    if (this.stateToggle) {
      this.reportService.open(this.report);
    } else {
      this.reportService.close(this.report);
    }
  }

  updateReport() {
    this.subscription = this.reportService.update(this.report)
      .subscribe(it => {
        this.report = it;
        if (this.report.state === 'opened') {
          this.stateToggle = true;
        } else {
          this.stateToggle = false;
        }
      });

  }
}
