import {Component, Input, OnInit} from '@angular/core';
import {Cashflow} from '../../../../../myfinance-tsclient-generated';
import { GridOptions } from 'ag-grid-community';
import {TransactionService} from '../../services/transaction.service';

@Component({
  selector: 'app-cashflowtable',
  templateUrl: './cashflowtable.component.html',
  styleUrls: ['./cashflowtable.component.scss']
})
export class CashflowtableComponent  implements OnInit {

  @Input() data: any;

  options: GridOptions;

  title = 'cashflows';
  cashflows: Array<Cashflow>;

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
        {headerName: 'Id', field: 'cashflowid' },
        {headerName: 'value', field: 'value'},
        {headerName: 'Instrument', field: 'instrument.description'}
      ]
    };
  }

  private loadData(): void {
    this.cashflows = new Array<Cashflow>();
    this.transactionservice.getTransactions().forEach(x => this.cashflows = this.cashflows.concat(x.cashflows))
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
