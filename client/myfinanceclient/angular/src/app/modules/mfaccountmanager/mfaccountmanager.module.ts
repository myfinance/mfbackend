import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IncomeexpenseseditorComponent } from './views/incomeexpenseseditor/incomeexpenseseditor.component';
import { TransactiontableComponent } from './views/incomeexpenseseditor/components/transactiontable/transactiontable.component';
import { AgGridModule } from 'ag-grid-angular';
import {MyFinanceService} from "../myfinance-tsclient-generated";
import {ConfigService} from "../../shared/services/config.service";
import {WidgetModule} from "../widget/widget.module";
import {DashboardModule} from "../dashboard/dashboard.module";
import {GridsterModule} from "angular-gridster2";
import { IncomeexpensesinputformComponent } from './views/incomeexpenseseditor/components/incomeexpensesinputform/incomeexpensesinputform.component';
import { ControllerComponent } from './views/incomeexpenseseditor/components/controller/controller.component';
import { CashflowtableComponent } from './views/incomeexpenseseditor/components/cashflowtable/cashflowtable.component';
import { ValuegraphComponent } from './views/incomeexpenseseditor/components/valuegraph/valuegraph.component';
import {TransactionService} from "./views/incomeexpenseseditor/services/transaction.service";
import {BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import {FormsModule} from "@angular/forms";
import {BsDropdownModule, ButtonsModule} from "ngx-bootstrap";

@NgModule({
  imports: [
    AgGridModule.withComponents([]),
    DashboardModule,
    WidgetModule,
    GridsterModule,
    CommonModule,
    FormsModule,
    BsDatepickerModule.forRoot(),
    ButtonsModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  declarations: [IncomeexpenseseditorComponent, TransactiontableComponent, IncomeexpensesinputformComponent, ControllerComponent, CashflowtableComponent, ValuegraphComponent],
  exports: [
    IncomeexpenseseditorComponent
  ],
  providers: [
    MyFinanceService, ConfigService, TransactionService
  ]
})
export class MfAccountManagerModule { }
