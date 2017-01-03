import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductService} from "../../domain/product/product.service";
import {TeamService} from '../../domain/team/team.service';
import {Product} from "../../domain/product/product";
import {User} from "../../domain/user/user";

@Component({
  selector: 'app-select-product-page',
  templateUrl: './select-product-page.component.html',
  styleUrls: ['./select-product-page.component.css']
})
export class SelectProductPageComponent implements OnInit {

  private teamId: string = '';
  private teamProducts: Product[] = [];
  private teamMembers: User[] = [];

  constructor(
    private productService: ProductService,
    private teamService: TeamService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
    });

    this.productService.showProducts(this.teamId)
      .subscribe(it => this.teamProducts = it);

    this.teamService.showMember(this.teamId)
      .subscribe(it => this.teamMembers = it);
  }

  createProduct() {
    const productId = 'product' + Math.floor(Math.random() * 10000);
    this.productService.createProduct(this.teamId, new Product(0, productId, null, null))
      .subscribe(it => {}, e => {}, () => {
        this.productService.showProducts(this.teamId)
          .subscribe(it => this.teamProducts = it);
      });
  }

}
