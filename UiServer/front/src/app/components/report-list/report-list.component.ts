import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
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
   * 現在開いているタブ
   * @type {string}
   */
  private activeTab: string = 'token';

  constructor(private reportService: ReportService,
              private productService: ProductService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
      this.productId = params['productId'];
    });

    this.reportService.list(this.productId)
      .subscribe(it => this.reports = it);

    this.productService.showProduct(this.teamId, this.productId)
      .subscribe(it => this.product = it);
  }

  focusTokenTab() {
    this.activeTab = 'token';
  }

  focusReportTab() {
    this.activeTab = 'report';
  }

}
