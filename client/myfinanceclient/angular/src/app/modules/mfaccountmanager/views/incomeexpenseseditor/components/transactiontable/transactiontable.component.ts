import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';
import {BooleanPipe} from "../../../../../widget/pipes/boolean.pipe";
import {DatePipe} from "@angular/common";
import * as Utils from '../../../../../../shared/utils';
import {TransactionService} from "../../../../services/transaction.service";

@Component({
  selector: 'transactiontable',
  templateUrl: './transactiontable.component.html',
  styleUrls: ['./transactiontable.component.scss'],
  providers: [TransactionService]
})
export class TransactiontableComponent implements OnInit, AfterViewInit {

  @Input() data: any;

  options: GridOptions;

  title = 'app';

  columnDefs = [
    {headerName: 'Make', field: 'make' },
    {headerName: 'Model', field: 'model' },
    {headerName: 'Price', field: 'price'}
  ];

  rowData = [
    { make: 'Toyota', model: 'Celica', price: 35000 },
    { make: 'Ford', model: 'Mondeo', price: 32000 },
    { make: 'Porsche', model: 'Boxter', price: 72000 }
  ];

  constructor(
    public transactionService: TransactionService) {
    this.options = <GridOptions>{
      columnDefs: [],
      rowData: [],
      treeData: true,
      getDataPath: (data) => data.path,
      autoGroupColumnDef: {
        headerName: 'AppRun ID',
        filter: 'agTextColumnFilter',
        filterValueGetter: params => params.data.path.join(',')
      },
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true,
      rowClassRules: {
        'success': (params) => params.data && params.data.appendts && !(params.data.failed),
        'error': (params) => params.data && params.data.failed,
        'active': (params) => params.data && !params.data.appendts && !params.data.failed
      }
    };
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.transactionService.search();
    if (this.data) this.update(this.data.data, this.data.columnSpecification);
  }

  onSelectionChanged(): void {
    this.transactionService.selectedTransaction = this.options.api.getSelectedRows()[0];
  }

  update(data: any[], columnSpecification: any[]): void {
    if (data.length > 0) {
      const visibleColumns = [
        'transactionid',
        'description',
        'transactiondate',
        'lastchanged'
      ];

      const columnDefs = [];

      columnDefs.push({
        headerName: '',
        cellClass: ['status-cell', 'lock-pinned'],
        pinned: 'left',
        lockPinned: true,
        width: 60,
        suppressMenu: true,
        floatingFilterComponentParams: {
          suppressFilterButton: true
        },
        sortable: false,
        resizable: false,
        filter: false
      });

      for (const spec of columnSpecification) {
        const columnDef = {
          headerName: spec.key,
          field: spec.key,
          hide: visibleColumns && visibleColumns.findIndex(c => c === spec.key) === -1 ? true : false
        };
        switch (spec.type) {
          case 'character':
          case 'boolean': {
            const pipe = new BooleanPipe();
            columnDef['filter'] = 'agSetColumnFilter';
            columnDef['newRowsAction'] = 'keep';
            columnDef['filterParams'] = {
              values: [pipe.transform(true), pipe.transform(false), null]
            };
            columnDef['valueGetter'] = (params) => {
              if (params.data && params.data[spec.key]) {
                return pipe.transform(params.data[spec.key]);
              } else {
                return pipe.transform(params.value);
              }
            };
            break;
          }
          case 'long':
          case 'double':
          case 'integer': {
            columnDef['type'] = 'numericColumn';
            columnDef['filter'] = 'agNumberColumnFilter';
            columnDef['cellClass'] = ['ag-numeric-cell'];
            columnDef['valueGetter'] = (params) => {
              if (params.data && params.data[spec.key]) {
                return +params.data[spec.key];
              } else return null;
            };
            break;
          }
          case 'date': {
            columnDef['filter'] = 'agDateColumnFilter';
            columnDef['filterParams'] = {};
            columnDef['cellRenderer'] = (params) => {
              if (params.value) {
                const pipe = new DatePipe('de');
                return pipe.transform(params.value);
              } else return null;
            };
            columnDef['valueGetter'] = (params) => {
              if (params.data && params.data[spec.key] && params.data[spec.key] !== null && params.data[spec.key] !== 'null') {
                const parts = params.data[spec.key].split('T')[0].split('-');
                if (parts.length === 3) return new Date(parts[0], +parts[1] - 1, parts[2]);
                else return null;
              } else return null;
            };
            break;
          }
          case 'timestamp': {
            columnDef['filter'] = 'agTextColumnFilter';
            columnDef['filterParams'] = {};
            columnDef['cellRenderer'] = (params) => {
              if (params.value) {
                const pipe = new DatePipe('de');
                return pipe.transform(params.value, 'dd.MM.yyyy hh:mm:ss');
              } else return null;
            };
            columnDef['valueGetter'] = (params) => {
              if (params.data && params.data[spec.key] && params.data[spec.key] !== null && params.data[spec.key] !== 'null') {
                return Utils.parseDatetime(params.data[spec.key]);
              } else return null;
            };
            break;
          }
          default: {
            columnDef['filter'] = 'agTextColumnFilter';
            columnDef['valueGetter'] = (params) => {
              if (params.data && params.data[spec.key] && params.data[spec.key] !== null && params.data[spec.key] !== 'null') {
                return params.data[spec.key];
              } else return null;
            };
            break;
          }
        }

        columnDefs.push(columnDef);
      }

      // sort columns
      const sortedColumnDefs = visibleColumns ? [columnDefs[0]] : columnDefs;
      if (visibleColumns) {
        for (const column of visibleColumns) {
          for (const columnDef of columnDefs) {
            if (column === columnDef.field) sortedColumnDefs.push(columnDef);
          }
        }
      }

      if (this.options.api) {
        this.options.api.setColumnDefs(sortedColumnDefs);
        //this.options.api.setRowData(this._buildTreeData(data));
        //this.applicationLogService.selectedLogEntry = null;
      }
    } else {
      if (this.options.api) {
        this.options.api.setColumnDefs([]);
        this.options.api.setRowData([]);
        //this.applicationLogService.selectedLogEntry = null;
      }
    }
  }
}
