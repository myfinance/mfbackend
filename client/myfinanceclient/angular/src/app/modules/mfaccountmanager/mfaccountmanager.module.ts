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
import { InstrumenteditorComponent } from './views/instrumenteditor/instrumenteditor.component';
import { InstrumentinputformComponent } from './views/instrumenteditor/components/instrumentinputform/instrumentinputform.component';
import { InstrumenttableComponent } from './views/instrumenteditor/components/instrumenttable/instrumenttable.component';
import { InstrumentcontrollerComponent } from './views/instrumenteditor/components/instrumentcontroller/instrumentcontroller.component';
import {InstrumentService} from "./views/instrumenteditor/services/instrument.service";
import {MyFinanceDataService} from "../../shared/services/myfinance-data.service";

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
  declarations: [TransactioneditorComponent,
    TransactiontableComponent,
    IncomeexpensesinputformComponent,
    ControllerComponent,
    CashflowtableComponent,
    ValuegraphComponent,
    InputformselectionComponent,
    InstrumenteditorComponent,
    InstrumentinputformComponent,
    InstrumenttableComponent,
    InstrumentcontrollerComponent],
  exports: [
    InstrumenteditorComponent,
    TransactioneditorComponent
  ],
  providers: [
    MyFinanceService, ConfigService, MyFinanceDataService, TransactionService, InstrumentService
  ]
})
export class MfAccountManagerModule { }
