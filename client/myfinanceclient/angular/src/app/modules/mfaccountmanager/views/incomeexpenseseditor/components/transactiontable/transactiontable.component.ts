import {Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';

import {TransactionListModel} from "../../../../../myfinance-tsclient-generated";
import {MyFinanceDataService} from "../../../../../../shared/services/myfinance-data.service";
import {DashboardService} from "../../../../../dashboard/services/dashboard.service";

@Component({
  selector: 'app-transactiontable',
  templateUrl: './transactiontable.component.html',
  styleUrls: ['./transactiontable.component.scss']
})
export class TransactiontableComponent implements OnInit{

  @Input() data: any;

  options: GridOptions;

  title = 'app';

  constructor(
    private myFinanceService: MyFinanceDataService,
    private widgetService: DashboardService) {

    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true,
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

  ngOnInit() {
    this.widgetService.handleLoading();
    if(this.myFinanceService.getIsInit()){
      this.loadData();
    } else {
      this.myFinanceService.transactionSubject.subscribe(
        () => {
          this.loadData()}
      )
    }
  }

  private loadData(): void {
    this.myFinanceService.getTransactions()
      .subscribe(
        (transactions: TransactionListModel) => {
          this.widgetService.handleDataPreparing();
          if (this.options.api) {
            this.options.api.setRowData(transactions.values);
          }
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
