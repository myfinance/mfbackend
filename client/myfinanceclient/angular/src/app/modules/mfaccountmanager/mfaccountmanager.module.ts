import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IncomeexpenseseditorComponent } from './views/incomeexpenseseditor/incomeexpenseseditor.component';
import { TransactiontableComponent } from './views/incomeexpenseseditor/components/transactiontable/transactiontable.component';
import { AgGridModule } from 'ag-grid-angular';
import {MyFinanceService} from "../myfinance-tsclient-generated";
import {ConfigService} from "../../shared/services/config.service";
import {WidgetModule} from "../widget/widget.module";
import {DashboardModule} from "../dashboard/dashboard.module";
import {MyFinanceCommonModule} from "../myfinance-common/myfinance-common.module";
import {GridsterModule} from "angular-gridster2";
import { IncomeexpensesinputformComponent } from './views/incomeexpenseseditor/components/incomeexpensesinputform/incomeexpensesinputform.component';

@NgModule({
  imports: [
    AgGridModule.withComponents([]),
    DashboardModule,
    WidgetModule,
    GridsterModule,
    CommonModule
  ],
  declarations: [IncomeexpenseseditorComponent, TransactiontableComponent, IncomeexpensesinputformComponent],
  exports: [
    IncomeexpenseseditorComponent
  ],
  providers: [
    MyFinanceService, ConfigService
  ]
})
export class MfAccountManagerModule { }
