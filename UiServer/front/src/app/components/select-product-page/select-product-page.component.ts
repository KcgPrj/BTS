import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductService} from "../../domain/product/product.service";
import {Product} from "../../domain/product/product";

@Component({
  selector: 'app-select-product-page',
  templateUrl: './select-product-page.component.html',
  styleUrls: ['./select-product-page.component.css']
})
export class SelectProductPageComponent implements OnInit {

  private teamId: string = '';
  private teamProducts: Product[] = [];

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
    });

    this.productService.showProducts(this.teamId)
      .subscribe(it => this.teamProducts = it);
  }

  createProduct() {
    this.productService.createProduct(this.teamId, new Product(0, 'hoge', null, null))
      .subscribe(it => {}, e => {}, () => {
        this.productService.showProducts(this.teamId)
          .subscribe(it => this.teamProducts = it);
      });
  }

}
