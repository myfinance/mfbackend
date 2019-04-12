import {Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {Cashflow, Instrument, Transaction, TransactionListModel} from "../../../../../myfinance-tsclient-generated";
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
    {headerName: 'Datum', field: 'transactiondate'},
    {headerName: 'Zuletzt geändert', field: 'lastchanged'},
    {headerName: 'Cashflows', field: 'cashflows'}
  ];
  instrument = {
    instrumentid: 1,
    description: 'testinstrument',
    isactive: true,
    treelastchanged: new Date(),
    instrumentType: Instrument.InstrumentTypeEnum.Giro
  };

  cashflows = [
    {cashflowid: 1,  instrument: this.instrument, value: 138.5},
    {cashflowid: 2,  instrument: this.instrument, value: -138.5}
  ]

  rowData = [
    { transactionid: 1, description: 'Celica', transactiondate: '2019-01-01', lastchanged: new Date(), cashflows: this.cashflows},
    { transactionid: 2, description: 'Mondeo', transactiondate: '2019-01-01', lastchanged: new Date(), cashflows: this.cashflows},
    { transactionid: 3, description: 'Boxter', transactiondate: '2019-01-01', lastchanged: new Date(), cashflows: this.cashflows}
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
