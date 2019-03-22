import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IncomeexpenseseditorComponent } from './incomeexpenseseditor/incomeexpenseseditor.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [IncomeexpenseseditorComponent],
  exports: [
    IncomeexpenseseditorComponent
  ]
})
export class MfAccountManagerModule { }
