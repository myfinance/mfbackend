import { NgModule } from '@angular/core';
import {CommonModule} from "@angular/common";
import {DashboardGridComponent} from "./components/dashboard-grid/dashboard-grid.component";
import {DashboardWidgetDirective} from "./directives/dashboard-widget.directive";
import {GridsterModule} from "angular-gridster2";
import {DashboardContentComponent} from "./components/dashboard-content/dashboard-content.component";
import {WidgetModule} from "../widget/widget.module";
import {MyFinanceCommonModule} from "../myfinance-common/myfinance-common.module";
import {DashboardService} from "./services/dashboard.service";

@NgModule({
  imports: [
    CommonModule,
    WidgetModule,
    MyFinanceCommonModule,
    GridsterModule
  ],
  exports: [
    DashboardGridComponent,
    DashboardContentComponent,
    DashboardWidgetDirective
  ],
  declarations: [
    DashboardGridComponent,
    DashboardContentComponent,
    DashboardWidgetDirective
  ],
  providers: [
    DashboardService
  ]
})
export class DashboardModule { }
