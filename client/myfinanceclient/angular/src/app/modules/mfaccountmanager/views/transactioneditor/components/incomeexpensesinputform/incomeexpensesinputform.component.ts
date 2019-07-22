import {Component, OnInit, ViewChild} from '@angular/core';
import {Instrument} from "../../../../../myfinance-tsclient-generated";
import {TransactionService} from "../../services/transaction.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-incomeexpensesinputform',
  templateUrl: './incomeexpensesinputform.component.html',
  styleUrls: ['./incomeexpensesinputform.component.scss']
})
export class IncomeexpensesinputformComponent implements OnInit {

  giros: Instrument[];
  budgets: Instrument[];
  @ViewChild('f')
  form:NgForm;
  giroDefault:Instrument;
  budgetDefault:Instrument;

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
    this.giroDefault = this.giros[0];
    this.budgets = this.transactionservice.getBudgets();
    this.budgetDefault = this.budgets[0];
  }

  onSubmit(){
    console.log(this.form)

  }
}
