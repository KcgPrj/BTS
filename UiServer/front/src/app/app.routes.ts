import { RouterModule, Routes } from '@angular/router';
import {SelectTeamPageComponent} from "./components/select-team-page/select-team-page.component";
import {SelectProductPageComponent} from "./components/select-product-page/select-product-page.component";
import {ModuleWithProviders} from "@angular/core";
import {ReportListComponent} from "./components/report-list/report-list.component";
import {ProductTopPageComponent} from "./components/product-top-page/product-top-page.component";
import {ReportDetailsComponent} from "./components/report-details-page/report-details-page.component";

const routerConfig: Routes = [
  { path: '', redirectTo: 'top', pathMatch: 'full'},
  { path: 'top', component: SelectTeamPageComponent},
  {
    path: ':teamId',
    component: SelectProductPageComponent,
    children: [
      { path: ':productId', component: ProductTopPageComponent },
      { path: ':productId/report', component: ReportListComponent},
      { path: ':productId/report/:reportId', component: ReportDetailsComponent},
    ]
  },
];

export const routes: ModuleWithProviders = RouterModule.forRoot(routerConfig, {useHash: true});
