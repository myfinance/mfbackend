import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {TransactionService} from '../../services/transaction.service';

@Component({
  selector: 'app-transactiontable',
  templateUrl: './transactiontable.component.html',
  styleUrls: ['./transactiontable.component.scss']
})
export class TransactiontableComponent implements OnInit, OnDestroy  {

  @Input() data: any;

  options: GridOptions;

  title = 'Transactions';

  constructor(private transactionservice: TransactionService) { }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      onGridReady: () => this.onGridReady(),
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
    // this.applicationLogService.selectedLogEntry = this.options.api.getSelectedRows()[0];
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

  ngOnDestroy(): void {
  }

}