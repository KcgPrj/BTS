import {Component, OnInit} from "@angular/core";
import {ReportService} from "../../domain/report/report.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Report} from "../../domain/report/report";
import {Subscription} from "rxjs";
import {User} from "../../domain/user/user";
import {TeamService} from "../../domain/team/team.service";

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
  private member: User[] = [];

  private titleToggle = false;
  private descToggle = false;
  private stateToggle = false;
  constructor(
    private reportService: ReportService,
    private teamService: TeamService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.productId = params['productId'];
      this.reportId = params['reportId'];
    });
    this.route.parent.params.forEach(params => {
      this.teamId = params['teamId'];
    });
    this.reloadReport();
  }

  reloadReport() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.subscription = this.reportService.find(this.reportId)
      .subscribe(it => {
        this.report = it;
        this.stateToggle = this.report.state === 'opened';
      });
    this.teamService.showMember(this.teamId)
      .subscribe(it => this.member = it);
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
    if (this.stateToggle) {
      this.reportService.open(this.report).subscribe(() => {}, e => console.log(e));
    } else {
      this.reportService.close(this.report).subscribe(() => {}, e => console.log(e));
    }
  }

  updateReport() {
    this.subscription = this.reportService.update(this.report)
      .subscribe(it => {
        this.report = it;
        this.stateToggle = this.report.state === 'opened';
      });

  }
}
