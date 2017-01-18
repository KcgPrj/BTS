import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {TeamService} from "./domain/team/team.service";
import {HttpWrapperService} from "./http-wrapper.service";
import {ProductService} from "./domain/product/product.service";
import {ReportService} from "./domain/report/report.service";
import {routes} from "./app.routes";
import {HashLocationStrategy, LocationStrategy, CommonModule} from "@angular/common";
import { SelectTeamPageComponent } from './components/select-team-page/select-team-page.component';
import { SelectProductPageComponent } from './components/select-product-page/select-product-page.component';
import { ReportListComponent } from './components/report-list/report-list.component';
import { ProductTopPageComponent } from './components/product-top-page/product-top-page.component';
import { ReportDetailsComponent } from './components/report-details-page/report-details-page.component';
import {UserService} from "./domain/user/user.service";

@NgModule({
  declarations: [
    AppComponent,
    SelectTeamPageComponent,
    SelectProductPageComponent,
    ReportListComponent,
    ProductTopPageComponent,
    ReportDetailsComponent
  ],
  imports: [
    routes,
    BrowserModule,
    FormsModule,
    HttpModule,
    CommonModule
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    TeamService,
    ProductService,
    ReportService,
    UserService,
    HttpWrapperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
