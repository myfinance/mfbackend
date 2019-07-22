import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactioneditorComponent } from './views/transactioneditor/transactioneditor.component';
import { TransactiontableComponent } from './views/transactioneditor/components/transactiontable/transactiontable.component';
import { AgGridModule } from 'ag-grid-angular';
import {MyFinanceService} from "../myfinance-tsclient-generated";
import {ConfigService} from "../../shared/services/config.service";
import {WidgetModule} from "../widget/widget.module";
import {DashboardModule} from "../dashboard/dashboard.module";
import {GridsterModule} from "angular-gridster2";
import { IncomeexpensesinputformComponent } from './views/transactioneditor/components/incomeexpensesinputform/incomeexpensesinputform.component';
import { ControllerComponent } from './views/transactioneditor/components/controller/controller.component';
import { CashflowtableComponent } from './views/transactioneditor/components/cashflowtable/cashflowtable.component';
import { ValuegraphComponent } from './views/transactioneditor/components/valuegraph/valuegraph.component';
import {TransactionService} from "./views/transactioneditor/services/transaction.service";
import {BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import {FormsModule} from "@angular/forms";
import {BsDropdownModule, ButtonsModule, TabsModule } from "ngx-bootstrap";
import { InputformselectionComponent } from './views/transactioneditor/components/inputformselection/inputformselection.component';

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
    TabsModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  declarations: [TransactioneditorComponent, TransactiontableComponent, IncomeexpensesinputformComponent, ControllerComponent, CashflowtableComponent, ValuegraphComponent, InputformselectionComponent],
  exports: [
    TransactioneditorComponent
  ],
  providers: [
    MyFinanceService, ConfigService, TransactionService
  ]
})
export class MfAccountManagerModule { }
