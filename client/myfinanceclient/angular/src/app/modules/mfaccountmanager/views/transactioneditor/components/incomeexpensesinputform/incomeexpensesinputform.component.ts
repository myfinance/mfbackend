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

  instruments: Instrument[]

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
    this.instruments = this.transactionservice.getInstruments();
  }


}
