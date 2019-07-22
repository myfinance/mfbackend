import { Component, OnInit } from '@angular/core';
import {Cashflow, Instrument, Transaction} from "../../../../../myfinance-tsclient-generated";
import {TransactionService} from "../../services/transaction.service";
import TransactionTypeEnum = Transaction.TransactionTypeEnum;

@Component({
  selector: 'app-incomeexpensesinputform',
  templateUrl: './incomeexpensesinputform.component.html',
  styleUrls: ['./incomeexpensesinputform.component.scss']
})
export class IncomeexpensesinputformComponent implements OnInit {

  giros: Instrument[]
  activeGiro: Instrument
  budgets: Instrument[]
  activeBudget: Instrument

  constructor(private transactionservice: TransactionService) { }

  ngOnInit() {
    if(this.transactionservice.getIsInit()){
      this.loadData();
    } else {
      this.transactionservice.instrumentSubject.subscribe(
        () => {
          this.loadData()}
      )
    }
  }

  private loadData(): void {
    this.giros = this.transactionservice.getGiros();
    this.budgets = this.transactionservice.getBudgets();
  }

  private getActiveGiroDesc(): string {
    return this.activeGiro == null ? "Select Giro" : this.activeGiro.description
  }

  private getActiveBudgetDesc(): string {
    return this.activeBudget == null ? "Select Budget" : this.activeBudget.description
  }

  private giroSelected(selectedInstrument: Instrument){
    this.activeGiro=selectedInstrument;
  }

  private budgetSelected(selectedInstrument: Instrument){
    this.activeBudget=selectedInstrument;
  }
}
