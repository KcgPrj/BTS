import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {TeamService} from "./domain/team/team.service";
import {HttpWrapperService} from "./http-wrapper.service";
import {ProductService} from "./domain/product/product.service";
import {ReportService} from "./domain/report/report.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    TeamService,
    ProductService,
    ReportService,
    HttpWrapperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
