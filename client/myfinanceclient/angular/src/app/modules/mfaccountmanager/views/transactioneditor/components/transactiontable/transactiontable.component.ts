import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {TransactionService} from '../../services/transaction.service';
import {Transaction} from '../../../../../myfinance-tsclient-generated';

@Component({
  selector: 'app-transactiontable',
  templateUrl: './transactiontable.component.html',
  styleUrls: ['./transactiontable.component.scss']
})
export class TransactiontableComponent implements OnInit, OnDestroy  {

  @Input() data: any;

  options: GridOptions;
  private gridApi;

  title = 'Transactions';

  constructor(private transactionservice: TransactionService) {
    this.transactionservice.transactionSubject.subscribe(
      () => {
        this.loadData()}
    )
  }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      onGridReady: (params) => this.onGridReady(params),
      floatingFilter: true,
      resizeable: true,
      sortable: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true,
      columnDefs: [
        {headerName: 'Id', field: 'transactionid' },
        {headerName: 'Beschreibung', field: 'description'},
        {headerName: 'Datum', field: 'transactiondate'},
        {headerName: 'Zuletzt geändert', field: 'lastchanged'},
        {headerName: 'TransactionType', field: 'transactionType'}
      ]
    };
  }

  private loadData(): void {
    if (this.options.api != null) {
      this.options.api.setRowData(this.transactionservice.getTransactions());
    }
  }

  onSelectionChanged(): void {
    const selectedTransaction: Transaction = this.options.api.getSelectedRows()[0];
    this.transactionservice.setTransactionfilter(selectedTransaction.transactionid);
  }

  onGridReady(params): void {
    this.gridApi = params.api;
    if (this.transactionservice.getIsInit()) {
      this.loadData();
    }
  }

  ngOnDestroy(): void {
  }

}
