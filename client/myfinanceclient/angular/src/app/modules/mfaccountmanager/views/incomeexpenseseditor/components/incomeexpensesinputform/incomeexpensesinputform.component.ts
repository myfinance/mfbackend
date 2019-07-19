import { Component, OnInit } from '@angular/core';
import {Cashflow, Instrument} from "../../../../../myfinance-tsclient-generated";
import {TransactionService} from "../../services/transaction.service";

@Component({
  selector: 'app-incomeexpensesinputform',
  templateUrl: './incomeexpensesinputform.component.html',
  styleUrls: ['./incomeexpensesinputform.component.scss']
})
export class IncomeexpensesinputformComponent implements OnInit {

  instruments: Instrument[]
  transactiontype = 'INCOMEEXPENSES';

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
