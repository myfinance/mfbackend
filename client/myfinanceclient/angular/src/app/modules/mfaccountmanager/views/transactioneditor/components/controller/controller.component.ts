import { Component, OnInit } from '@angular/core';
import {BsDatepickerConfig} from "ngx-bootstrap";
import {TransactionService} from "../../services/transaction.service";

@Component({
  selector: 'app-controller',
  templateUrl: './controller.component.html',
  styleUrls: ['./controller.component.scss']
})
export class ControllerComponent implements OnInit {

  bsConfig: Partial<BsDatepickerConfig>;
  daterange:  Array<Date>;
  items: string[] = [
    'The first choice!',
    'And another choice for you.',
    'but wait! A third!'
  ];

  constructor(private transactionservice: TransactionService) {
    this.daterange = this.transactionservice.getDaterange()
  }

  ngOnInit() {
    this.bsConfig = Object.assign({}, { containerClass: 'theme-default', rangeInputFormat: 'YYYY-MM-DD'});
  }

  onValueChange(value: Date[]): void {
    this.transactionservice.setDaterange(value);
  }

}
