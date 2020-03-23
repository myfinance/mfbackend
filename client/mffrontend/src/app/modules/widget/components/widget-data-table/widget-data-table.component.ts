import { Component, OnInit, OnDestroy, AfterViewInit, Input, ViewChild, ElementRef } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Subscription } from 'rxjs/Subscription';
import { Subject } from 'rxjs/Subject';
import { GridOptions } from 'ag-grid-community';
import dc from 'dc';

import { DcService } from '../../services/dc.service';
import { FinancialNumberPipe } from '../../pipes/financial-number.pipe';
import {DashboardGridComponent} from "../../../dashboard/components/dashboard-grid/dashboard-grid.component";

@Component({
  selector: 'app-widget-data-table',
  templateUrl: './widget-data-table.component.html',
  styleUrls: ['./widget-data-table.component.scss']
})
export class WidgetDataTableComponent implements OnInit, OnDestroy, AfterViewInit {

  _gridOptions: GridOptions;
  private _subscriptions: Subscription[] = [];

  @Input()
  config

  @Input()
  hideHeader

  @Input()
  resized: Subject<any>;

  @ViewChild('table', {static: false}) table: ElementRef;

  constructor(private _dcService: DcService) { }

  ngOnInit() {
    this._subscriptions.push(
      this.resized.subscribe(event => {
        if(this.config.uuid == event.uuid) {
          this.resize();
        }
      })
    );

    // Update data grid when crossfilters changed.
    this._subscriptions.push(
      this._dcService.filtered.subscribe( event => {
        this.refreshData();
      })
    );

    this._gridOptions = <GridOptions>{
      columnDefs: this._createColumnDefs(),
      onRowDataChanged: (params) => this.refreshView(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true
    };
  }

  getRowHeight(row) {
    return 50;
  }

  ngAfterViewInit() {
    // Refresh data after view init with a timeout.
    // There is an issue with the onGridReady event firing to early,
    // this means the columns can't correctly be fitted to the grid's size.
    // Waiting is a workaround to make this work.
    setTimeout(this._onGridReady.bind(this), 500);
  }

  private _onGridReady(): void {
    this._dcService.registerChart(this);
    this.refreshData();
  }

  private _createColumnDefs(): any[] {
    let columns = [];

    for(let column of this.config.columns) {
      let columnConfig = {
        field: column.value,
        headerName: column.title,
        floatingFilterComponentParams: {
          suppressFilterButton: true
        },
        suppressMenu: false
      }

      switch(column.type) {
        case 'number': {
            columnConfig['valueFormatter'] = (params) => {
              if(!Number.isNaN(Number(params.data[column.value]))) {
                let pipe = new DecimalPipe('de');
                return pipe.transform(params.data[column.value], '1.2-2');
              }
            };
            columnConfig['valueGetter'] = (params) => {
              if(!Number.isNaN(Number(params.data[column.value]))) {
                return Math.round(params.data[column.value] * 100) / 100;
              }
            };
          break;
        }
        case 'financial-number': {
          columnConfig['valueGetter'] = (params) => {
            let pipe = new FinancialNumberPipe();
            return pipe.transform(params.data[column.value]);
          };
          break;
        }
        default: break;
      }

      switch(column.filter.type) {
        case 'number': {
          columnConfig['filter'] = 'number';
          columnConfig['floatingFilterComponentParams'] = {
            suppressFilterButton: false
          }
          break;
        }
        default: {
          columnConfig['filter'] = 'text';
          columnConfig['filterParams'] = {
            filterOptions:['startsWith']
          };
          break;
        }
      }

      columns.push(columnConfig);
    }

    return columns;
  }

  private _createRowData(): any[] {
    let dimension = this._dcService.getDimension(this.config.dimension);
    return dimension.top(Infinity);
  }

  _handleFilterChanged(event): void {
    let filters = event.api.getFilterModel();
    let dimension = this._dcService.getDimension(this.config.dimension);
    let dimensionCount = dimension.top(Infinity).length;

    dimension.filterFunction(row => {
      let match = true;
      for(let column of this._gridOptions.columnDefs) {
        let filter = filters[column['field']];
        if(filter) {
          switch(filter.filterType) {
            case 'number': {
              switch(filter.type) {
                case 'lessThanOrEqual': {
                  if(!(row[column['field']] <= filter.filter)) {
                    match = false;
                  }
                  break;
                }
                case 'lessThan': {
                  if(!(row[column['field']] < filter.filter)) {
                    match = false;
                  }
                  break;
                }
                case 'greaterThanOrEqual': {
                  if(!(row[column['field']] >= filter.filter)) {
                    match = false;
                  }
                  break;
                }
                case 'greaterThan': {
                  if(!(row[column['field']] > filter.filter)) {
                    match = false;
                  }
                  break;
                }
                case 'inRange': {
                  if(!(row[column['field']] < filter.filter && row[column['field']] > filter.filterTo)) {
                    match = false;
                  }
                  break;
                }
                case 'notEqual': {
                  if(row[column['field']] == filter.filter) {
                    match = false;
                  }
                  break;
                }
                default: {
                  if(!(row[column['field']] == filter.filter)) {
                    match = false;
                  }
                  break;
                }
              }
              break;
            }
            default: {
              if(!String(row[column['field']]).toLowerCase().startsWith(String(filter.filter).toLowerCase())) {
                match = false;
                break;
              }
            }
          }
        }
      }
      return match;
    });

    if(dimension.top(Infinity).length !== dimensionCount) {
      this._dcService.handleFiltered(this, filters);
    }

    dc.redrawAll();
  }

  refreshData(): void {
    this._gridOptions.rowData = this._createRowData();
    this._gridOptions.api.setRowData(this._gridOptions.rowData);
    this._gridOptions.api.setFilterModel(this._dcService.filters[this.config.dimension]);
  }

  filters(): any {
    return this._gridOptions.api.getFilterModel();
  }

  filter(filterModels: any[]): void {
    this._gridOptions.api.setFilterModel(filterModels[0]);
  }

  resize(): void {
    this.refreshView();
  }

  refreshView(): void {
    if(this.config.view === 'fit') this._gridOptions.api.sizeColumnsToFit();
  }

  ngOnDestroy() {
    for(let subscription of this._subscriptions) {
      subscription.unsubscribe();
    }
  }
}
