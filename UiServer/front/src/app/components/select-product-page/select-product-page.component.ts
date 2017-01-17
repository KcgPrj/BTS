import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductService} from "../../domain/product/product.service";
import {TeamService} from '../../domain/team/team.service';
import {Product} from "../../domain/product/product";
import {User} from "../../domain/user/user";
import {ModalService} from "../../modal.service";
import {UserService} from "../../domain/user/user.service";

@Component({
  selector: 'app-select-product-page',
  templateUrl: './select-product-page.component.html',
  styleUrls: ['./select-product-page.component.css'],
  providers: [
    ModalService
  ],
})
export class SelectProductPageComponent implements OnInit {

  private teamId: string = '';
  private teamProducts: Product[] = [];
  private teamMembers: User[] = [];
  private allUsers: User[] = [];


  private memberForm: number;
  private productForm: Product = new Product(0, '', null, null);

  constructor(
    private productService: ProductService,
    private teamService: TeamService,
    private route: ActivatedRoute,
    private userService: UserService,
    private modalService: ModalService) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
    });

    this.productService.showProducts(this.teamId)
      .subscribe(it => this.teamProducts = it);

    this.teamService.showMember(this.teamId)
      .subscribe(it => this.teamMembers = it);

    this.userService.fetchAll()
      .subscribe(it => this.allUsers = it);
  }

  createProduct() {
    if (this.productForm.productName == '') {
      return;
    }
    this.productService.createProduct(this.teamId, this.productForm)
      .subscribe(it => {}, e => {}, () => {
        this.productService.showProducts(this.teamId)
          .subscribe(it => this.teamProducts = it);
      });
    this.productForm = new Product(0, '', null, null);
    this.modalService.closeProductModal();
  }

  addMember() {
    this.teamService.join(this.teamId, this.memberForm).subscribe(it => {}, e => {}, () => {
      this.teamService.showMember(this.teamId)
        .subscribe(it => this.teamMembers = it);
    });
    this.modalService.closeMemberModal();
  }
}
