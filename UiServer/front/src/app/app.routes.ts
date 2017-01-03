import {RouterModule, Routes} from '@angular/router';
import {SelectTeamPageComponent} from "./components/select-team-page/select-team-page.component";
import {SelectProductPageComponent} from "./components/select-product-page/select-product-page.component";
import {ModuleWithProviders} from "@angular/core";
import {ReportListComponent} from "./components/report-list/report-list.component";

const routerConfig: Routes = [
  { path: '', redirectTo: 'top', pathMatch: 'full'},
  { path: 'top', component: SelectTeamPageComponent},
  {
    path: 'team',
    children: [
      {
        path: ':teamId',
        component: SelectProductPageComponent,
        children: [
          {path: ':productId', component: ReportListComponent},
        ],
      },
    ],
  },
];

export const routes: ModuleWithProviders = RouterModule.forRoot(routerConfig, {useHash: true});
