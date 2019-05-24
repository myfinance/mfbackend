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
  daterange: Date[];

  constructor(private transactionservice: TransactionService) { }

  ngOnInit() {
    this.bsConfig = Object.assign({}, { containerClass: 'theme-default', rangeInputFormat: 'YYYY-MM-DD',  });
  }

}
