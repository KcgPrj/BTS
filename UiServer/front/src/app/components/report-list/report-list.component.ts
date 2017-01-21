import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, NavigationEnd} from "@angular/router";
import {ReportService} from "../../domain/report/report.service";
import {Report} from "../../domain/report/report";
import {ProductService} from "../../domain/product/product.service";
import {Product} from "../../domain/product/product";

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent implements OnInit {

  private productId: number;
  private teamId: string;

  private product: Product;
  private reports: Report[] = [];

  /**
   * 検索テキストに応じて絞り込まれたレポートの配列
   * @type {Array}
   */
  private filteredReports: Report[] = [];
  /**
   * 検索テキストボックスの文字列
   * @type {string}
   */
  private searchText: string = '';

  /**
   * 現在開いているタブ
   * @type {string}
   */
  private activeTab: string = 'report';

  constructor(private reportService: ReportService,
              private productService: ProductService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    //プロダクトトップページからべつのプロダクトトップページへ移動した時にコンポーネントが作り直されないので
    //routerのイベントをSubscribeして更新する
    this.router.events.subscribe(e => {
      if (e instanceof NavigationEnd) {
        this.updateReportList();
        this.searchText = '';
      }
    });
  }

  updateReportList() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
      this.productId = params['productId'];
    });

    this.reportService.list(this.productId)
      .subscribe(it => {
        this.reports = it;
        this.filterReports();
      });

    this.productService.showProduct(this.teamId, this.productId)
      .subscribe(it => this.product = it);
  }

  /**
   * 検索文字列に応じてレポートを絞り込む
   */
  filterReports(): void {
    if (this.searchText === '') {
      this.filteredReports = this.reports.concat();
    }

    this.filteredReports = this.reports.filter((report: Report) => {
      return report.title.includes(this.searchText);
    });
  }

  focusTokenTab() {
    this.activeTab = 'token';
  }

  focusReportTab() {
    this.activeTab = 'report';
  }

  openDetails(report: Report) {
    this.router.navigate([report.reportId], {relativeTo: this.route});
  }

}
