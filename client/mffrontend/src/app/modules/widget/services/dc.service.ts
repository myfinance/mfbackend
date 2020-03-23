import { Injectable, OnDestroy } from '@angular/core';
import * as crossfilter from 'crossfilter2';
import { timeFormat } from 'd3-time-format';
import {Subject} from "rxjs/Subject";
import {ActivatedRoute, Router} from "@angular/router";
import { Location } from '@angular/common';

const d3FormatDateDim = timeFormat('%Y-%m-%d');

@Injectable()
export class DcService implements OnDestroy {

  filters: any = {};
  private _data;
  private _config;
  private _ndx;
  private _dimensions;
  private _charts: any[] = [];

  filtered: Subject<any> = new Subject();

  constructor(private _activatedRoute: ActivatedRoute, private _router: Router, private _location: Location) { }

  get data() {
    return this._data;
  }

  load(config, data: any[], handler): void {
    this._config = config;
    this._createCrossfilter(data);
    this._createDimensions();
    handler();
  }

  private _createCrossfilter(data: any[]): void {
    let rowFn = (d) => {
      let obj = {};
      this._config.data.columns.map( column => {
        switch(column.type) {
          /* case 'date': {
            obj[column.key] = d3ParseDate(d[column.key]);
            break;
          }
          case 'datetime': {
            obj[column.key] = d3ParseDatetime(d[column.key]);
            break;
          } */
          case 'double': {
            obj[column.key] = +d[column.key];
            break;
          }
          default: {
            obj[column.key] = d[column.key];
            break;
          }
        }
      });
      return obj;
    };

    this._data = data.map(rowFn);
    this._ndx = crossfilter(this._data);
  }

  private _createDimensions(): void {
    this._dimensions = {};
    for(let dimension of this._config.dimensions) {
      this._dimensions[dimension.id] = this._ndx.dimension(d => {
        let values = dimension.value.map(v => {
          switch(this._config.data.columns[v]) {
            case 'date': {
              return d3FormatDateDim(d[v]);
            }
            default: {
              if(v.length == 0) {
                return d;
              }
              return d[v];
            }
          }
        });

        if(values.length > 1) {
          return values;
        }
        return values[0];
      });
    }
  }


  private _applyURLFilter(chart: any): void {
    let urlFilters = this._activatedRoute.snapshot.queryParams['filters'];
    if(urlFilters) {
      this.filters = JSON.parse(atob(decodeURIComponent(urlFilters)));
      for(let filter in this.filters) {
        if(filter == chart.config.dimension) {
          chart.filter([this.filters[chart.config.dimension]]);
        }
      }
    }
  }

  registerChart(chart: any): void {
    this._charts.push(chart);
    // Apply URL filters after registration.
    this._applyURLFilter(chart);
  }

  getChart(uuid: string): any {
    for(let chart of this._charts) {
      if(chart.config.uuid == uuid) return chart;
    }
  }

  getDimension(id: string): any {
    return this._dimensions[id];
  }

  getDimensionConfig(id: string): any {
    for(let dimension of this._config.dimensions) {
      if(dimension.id == id) return dimension;
    }
  }

  handleFiltered(chart, filter): void {
    if(Object.keys(chart.filters()).length === 0) {
      delete this.filters[chart.config.dimension];
    } else {
      this.filters[chart.config.dimension] = chart.filters();
    }

    this.filtered.next({ chart: chart, filter: filter });

    let url;
    if(Object.keys(chart.filters()).length === 0) {
      url = this._router
        .createUrlTree([], { relativeTo: this._activatedRoute }).toString();
    } else {
      url = this._router
        .createUrlTree([], { queryParams: { filters: encodeURIComponent(btoa(JSON.stringify(this.filters))) }, relativeTo: this._activatedRoute }).toString();
    }

    this._location.go(url);
  }

  ngOnDestroy() {
    this._data = null;
    this._config = null;
    this._ndx = null;
    this._dimensions = null;
  }

}
