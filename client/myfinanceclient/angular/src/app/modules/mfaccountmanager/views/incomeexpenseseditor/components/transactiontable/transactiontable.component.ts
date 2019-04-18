import {Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {Cashflow, Instrument, Transaction, TransactionListModel} from "../../../../../myfinance-tsclient-generated";
import {MyFinanceDataService} from "../../../../../../shared/services/myfinance-data.service";
import {WidgetService} from "../../../../../widget/services/widget.service";

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
    private myFinanceService: MyFinanceDataService,
    private widgetService: WidgetService) {

    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true
    };

  }

  ngOnInit() {
    this.myFinanceService.ngOnInit();
    this.widgetService.handleLoading();
    if(this.myFinanceService.getIsInit()){
      this.loadData();
    } else {
      this.myFinanceService.transactionSubject.subscribe(
        (configUpdated:boolean) => {
          this.loadData()}
      )
    }
  }

  private loadData(): void {
    this.myFinanceService.getTransactions()
      .subscribe(
        (transactions: TransactionListModel) => {
          this.widgetService.handleDataPreparing();
          this.rowData = transactions.values;
          this.widgetService.handleDataLoaded();
        },
        (errResp) => {
          console.error('error', errResp);
          this.widgetService.handleDataNotLoaded(errResp);

        }), (errResp) => {
      console.error('error', errResp);
      this.widgetService.handleDataNotLoaded(errResp);
    }
  }

  onSelectionChanged(): void {
    //this.applicationLogService.selectedLogEntry = this.options.api.getSelectedRows()[0];
  }

}
