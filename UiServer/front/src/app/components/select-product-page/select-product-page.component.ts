import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-select-product-page',
  templateUrl: './select-product-page.component.html',
  styleUrls: ['./select-product-page.component.css']
})
export class SelectProductPageComponent implements OnInit {

  private teamId: string = '';

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.forEach(params => {
      this.teamId = params['teamId'];
    });
  }

}
