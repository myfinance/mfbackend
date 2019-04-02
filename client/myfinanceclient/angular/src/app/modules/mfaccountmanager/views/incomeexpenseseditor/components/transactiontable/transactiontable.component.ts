import {Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {TransactionListModel} from "../../../../../myfinance-tsclient-generated";
import {MyFinanceDataService} from "../../../../../../shared/services/myfinance-data.service";

@Component({
  selector: 'transactiontable',
  templateUrl: './transactiontable.component.html',
  styleUrls: ['./transactiontable.component.scss'],
})
export class TransactiontableComponent implements OnInit{

  @Input() data: any;

  options: GridOptions;

  title = 'app';

  columnDefs = [
    {headerName: 'Id', field: 'transactionid' },
    {headerName: 'Beschreibung', field: 'description'},
    {headerName: 'Datum', field: 'transactiondate'}
  ];

  rowData = [
    { transactionid: 1, description: 'Celica', transactiondate: '2019-01-01' },
    { transactionid: 2, description: 'Mondeo', transactiondate: '2019-01-01' },
    { transactionid: 3, description: 'Boxter', transactiondate: '2019-01-01' }
  ];

  constructor(
    private myFinanceService: MyFinanceDataService) {

    this.options = <GridOptions>{
      //columnDefs: this.columnDefs,
      //rowData: this.rowData,
      //getDataPath: (data) => data.path,
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true
    };//

  }

  ngOnInit() {
    this.myFinanceService.ngOnInit()
    this.myFinanceService.transactionSubject.subscribe(
      (configUpdated:boolean) => {
        this.myFinanceService.getTransactions()
          .subscribe(
            (transactions: TransactionListModel) => {
              this.rowData = transactions.values;
            },
            (errResp) => {
              console.error('error', errResp);

            }), (errResp) => {
          console.error('error', errResp);
      }}
    )
  }

  onSelectionChanged(): void {
    //this.applicationLogService.selectedLogEntry = this.options.api.getSelectedRows()[0];
  }

}
