import { Injectable, OnDestroy } from '@angular/core';
import crossfilter from 'crossfilter2';
import { timeFormat } from 'd3-time-format';
import { timeParse, utcParse } from 'd3-time-format';

const d3ParseDateDimension = timeParse('%Y-%m-%d');
const d3FormatDateDim = timeFormat('%Y-%m-%d');

@Injectable()
export class DcService implements OnDestroy {

  private _data;
  private _config;
  private _ndx;
  private _dimensions;

  constructor() { }

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

  getDimension(id: string): any {
    return this._dimensions[id];
  }

  ngOnDestroy() {
    this._data = null;
    this._config = null;
    this._ndx = null;
    this._dimensions = null;
  }

}
