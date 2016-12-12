import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import {TeamService} from "./team.service";
import {HttpWrapperService} from "./http-wrapper.service";

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
    HttpWrapperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
