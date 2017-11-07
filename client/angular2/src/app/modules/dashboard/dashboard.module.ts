import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GenericDashboardViewComponent } from './views/generic-dashboard-view/generic-dashboard-view.component';
import {ROUTES} from "./dashboard.routes";
import {DataService} from "../../services/data.service";

@NgModule({
  imports: [
    RouterModule.forChild(ROUTES)
  ],
  exports: [
    RouterModule
  ],
  declarations: [
    GenericDashboardViewComponent
  ],
  providers: [
    DataService
  ]
})
export class DashboardModule { }
