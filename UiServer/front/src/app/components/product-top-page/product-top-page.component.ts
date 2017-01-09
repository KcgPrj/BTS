import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../domain/product/product.service";
import {ActivatedRoute, Router, NavigationEnd} from "@angular/router";
import {Subscription} from "rxjs";
import {Product} from "../../domain/product/product";
import {ReportService} from "../../domain/report/report.service";
import {ProductToken} from "../../domain/product/product-token";
import {Report} from "../../domain/report/report";
import {User} from "../../domain/user/user";

@Component({
  selector: 'app-product-top-page',
  templateUrl: './product-top-page.component.html',
  styleUrls: ['./product-top-page.component.css']
})
export class ProductTopPageComponent implements OnInit {

  private teamId: string;
  private productId: number;

  private subscription: Subscription;
  private product: Product;

  constructor(
    private productService: ProductService,
    private reportService: ReportService,
    private route: ActivatedRoute,
    private router: Router) {

  }

  createTestReport() {
    this.reportService.createReport(this.product.token, new Report(0, "test", "desc", "", new User(1, '', ''), "version", "stacktace", "log", "runtime", this.product, "opened")).subscribe(it => console.log(it), e => console.log(e));
  }

  ngOnInit() {
    //プロダクトトップページからべつのプロダクトトップページへ移動した時にコンポーネントが作り直されないので
    //routerのイベントをSubscribeして更新する
    this.router.events.subscribe(e => {
      if (e instanceof NavigationEnd) {
        this.updateProduct();
      }
    });
    this.updateProduct();
  }

  updateProduct() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
      this.productId = params['productId'];
    });

    if (this.subscription != null) {
      this.subscription.unsubscribe();
    }

    this.subscription = this.productService.showProduct(this.teamId, this.productId)
      .subscribe(it => this.product = it);
  }

}
