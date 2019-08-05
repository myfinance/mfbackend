import {Component, OnInit, ViewChild} from '@angular/core';
import {Instrument} from "../../../../../myfinance-tsclient-generated";
import {TransactionService} from "../../services/transaction.service";
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";

@Component({
  selector: 'app-incomeexpensesinputform',
  templateUrl: './incomeexpensesinputform.component.html',
  styleUrls: ['./incomeexpensesinputform.component.scss']
})
export class IncomeexpensesinputformComponent implements OnInit {

  giros: Instrument[];
  budgets: Instrument[];
  giroDefault:Instrument;
  budgetDefault:Instrument;
  incomeExpensesForm: FormGroup;

  constructor(private transactionservice: TransactionService) { }

  ngOnInit() {
    this.incomeExpensesForm = new FormGroup({
      'giro': new FormControl(null, Validators.required),
      'budget': new FormControl(null),
      'value': new FormControl(null, [Validators.required]),
    });
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
    console.log(this.incomeExpensesForm)
    this.transactionservice.saveIncomeExpenses("bla", 9, 10, 5, new Date())
    this.incomeExpensesForm.reset();
  }
}
