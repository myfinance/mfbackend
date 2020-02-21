import {Component, Input, OnInit} from '@angular/core';
import {Cashflow} from '../../../../../myfinance-tsclient-generated';
import { GridOptions } from 'ag-grid-community';
import {TransactionService} from '../../services/transaction.service';

interface MyCashflow {
  transactionId: number;
  cashflowId: number;
  value: number;
  instrument: string;
}

@Component({
  selector: 'app-cashflowtable',
  templateUrl: './cashflowtable.component.html',
  styleUrls: ['./cashflowtable.component.scss']
})
export class CashflowtableComponent  implements OnInit {

  @Input() data: any;

  options: GridOptions;

  title = 'cashflows';

  cashflows: Array<MyCashflow>;

  constructor( private transactionservice: TransactionService) { }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      floatingFilter: true,
      resizeable: true,
      sortable: true,
      sideBar: 'filters',
      onGridReady: () => this.onGridReady(),
      suppressPropertyNamesCheck: true,
      columnDefs: [
        {headerName: 'Id', field: 'cashflowId' },
        {headerName: 'value', field: 'value'},
        {headerName: 'Instrument', field: 'instrument'},
        {headerName: 'TransactionId', field: 'transactionId'}
      ]
    };
  }

  private loadData(): void {
    this.cashflows = new Array<MyCashflow>();
    this.transactionservice.getTransactions().forEach(x => x.cashflows.forEach(c => this.cashflows.push({
      transactionId: x.transactionid,
      value: c.value,
      cashflowId: c.cashflowid,
      instrument: c.instrument.description })))
    if (this.options.api) {
      this.options.api.setRowData(this.cashflows);
    }
  }

  onGridReady(): void {
    if (this.transactionservice.getIsInit()) {
      this.loadData();
    } else {
      this.transactionservice.transactionSubject.subscribe(
        () => {
          this.loadData()}
      )
    }
  }
}
