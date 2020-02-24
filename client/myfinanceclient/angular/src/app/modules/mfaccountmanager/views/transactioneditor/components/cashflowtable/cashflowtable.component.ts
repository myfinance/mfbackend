import {Component, Input, OnInit} from '@angular/core';
import {GridApi, GridOptions} from 'ag-grid-community';
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
  private gridApi;

  title = 'cashflows';

  cashflows: Array<MyCashflow>;

  constructor( private transactionservice: TransactionService) {
    this.transactionservice.transactionSubject.subscribe(
      () => {
        this.loadData()}
    );
    this.transactionservice.transactionFilterSubject.subscribe(
      () => {
        this.loadData()}
    )
  }

  ngOnInit() {
    this.options = <GridOptions>{
      context: {parentComponent: this},
      rowSelection: 'single',
      floatingFilter: true,
      resizeable: true,
      sortable: true,
      onGridReady: (params) => this.onGridReady(params),
      suppressPropertyNamesCheck: true,
      columnDefs: [
        {headerName: 'Id', field: 'cashflowId', filter: true },
        {headerName: 'value', field: 'value', filter: true},
        {headerName: 'Instrument', field: 'instrument', filter: true},
        {headerName: 'TransactionId', field: 'transactionId', filter: true }
      ]
    };
  }

  private loadData(): void {
    this.cashflows = new Array<MyCashflow>();
    let filteredTransactions = this.transactionservice.getTransactions();
    if (this.transactionservice.getTransactionfilter() !== -1) {
      filteredTransactions = filteredTransactions.filter(i => i.transactionid === this.transactionservice.getTransactionfilter());
    }
    filteredTransactions.forEach(x => x.cashflows.forEach(c => this.cashflows.push({
      transactionId: x.transactionid,
      value: c.value,
      cashflowId: c.cashflowid,
      instrument: c.instrument.description })))
    if (this.gridApi) {
      this.gridApi.setRowData(this.cashflows);
    }
  }

  onGridReady(params): void {
    this.gridApi = params.api;
    if (this.transactionservice.getIsInit()) {
      this.loadData();
    }
  }
}
