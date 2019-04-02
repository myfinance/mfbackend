import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IncomeexpenseseditorComponent } from './views/incomeexpenseseditor/incomeexpenseseditor.component';
import { TransactiontableComponent } from './views/incomeexpenseseditor/components/transactiontable/transactiontable.component';
import { AgGridModule } from 'ag-grid-angular';
import {MyFinanceService} from "../myfinance-tsclient-generated";
import {ConfigService} from "../../shared/services/config.service";

@NgModule({
  imports: [
    AgGridModule.withComponents([]),
    CommonModule
  ],
  declarations: [IncomeexpenseseditorComponent, TransactiontableComponent],
  exports: [
    IncomeexpenseseditorComponent
  ],
  providers: [
    MyFinanceService, ConfigService
  ]
})
export class MfAccountManagerModule { }
