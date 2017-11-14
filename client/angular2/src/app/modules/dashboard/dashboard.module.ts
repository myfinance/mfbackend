import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GridsterModule } from 'angular-gridster2';
import { GenericDashboardViewComponent } from './views/generic-dashboard-view/generic-dashboard-view.component';
import {ROUTES} from "./dashboard.routes";
import {DashboardGridComponent} from "./shared/components/dashboard-grid/dashboard-grid.component";
import {WidgetModule} from "../widget/widget.module";
import {CommonModule} from "@angular/common";
import {DashboardTopMenuComponent} from "./shared/components/dashboard-top-menu/dashboard-top-menu.component";
import {CsvService} from "./shared/services/csv.service";
import {DataService} from "./shared/services/data.service";
import {MyFinanceCommonModule} from "../myfinance-common/myfinance-common.module";
import {CollapseModule} from "ngx-bootstrap";

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(ROUTES),
    CollapseModule.forRoot(),
    GridsterModule,
    MyFinanceCommonModule,
    WidgetModule
  ],
  exports: [
    RouterModule
  ],
  declarations: [
    DashboardGridComponent,
    GenericDashboardViewComponent,
    DashboardTopMenuComponent
  ],
  providers: [
    DataService,
    CsvService
  ]
})
export class DashboardModule { }