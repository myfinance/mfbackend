import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {UUID} from "angular2-uuid";
import {Subject} from "rxjs/Subject";
import {DcService} from "../../../modules/widget/services/dc.service";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";
import {Position} from "../../../shared/models/position";

@Component({
  selector: 'app-gridexp',
  templateUrl: './gridexp.component.html',
  styleUrls: ['./gridexp.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GridexpComponent implements OnInit {
  _loading: boolean = false;
  _preparing: boolean = false;
  _dataLoaded: boolean = false;

  private _config= {
    data: {
      columns: [
        { key: 'isin', type: 'string' },
        { key: 'desc', type: 'string' },
        { key: 'price', type: 'double' },
        { key: 'amount', type: 'double' },
        { key: 'valdate', type: 'date' },
      ]
    },
    dimensions: [
      { id: 'd42cafb0-3889-44c5-9420-9020174b05ce', value: [ '' ] },
      { id: '81e6ea16-6cba-40a0-8204-0e1f8f103ca5', value: [ 'isin' ] },
      { id: '5e9c4eb3-8526-42f3-9ea8-091148b2dd92', value: [ 'valdate' ] },
      { id: '5304029d-3996-4aff-bafc-11098e3f3d2b', value: [ 'isin', 'valdate' ] }
    ]
  };


  private _widgetConfig= {
    title: 'Positions',
    dimension: 'd42cafb0-3889-44c5-9420-9020174b05ce',
    view: 'fit',
    columns: [
      {
        title: 'ISIN',
        type: 'string',
        value: 'isin',
        filter: {
          type: 'text'
        },
        sortable: true
      },
      {
        title: 'Beschreibung',
        value: 'desc',
        filter: {
          type: 'text'
        },
        sortable: true
      },
      {
        title: 'Price',
        value: 'price',
        type: 'financial-number',
        filter: {
          type: 'text'
        },
        sortable: true
      },
      {
        title: 'Anzahl',
        value: 'amount',
        type: 'financial-number',
        filter: {
          type: 'text'
        },
        sortable: true
      },
      {
        title: 'Datum',
        value: 'valdate',
        type: 'date',
        filter: {
          type: 'text'
        },
        sortable: true
      }
    ]
  };
  private _data;
  private _resizedSubject: Subject<any> = new Subject();

  constructor(private _dcService: DcService, private dataService: MyFinanceDataService) { }

  ngOnInit() {
    this.loadData();
  }

  widgetLoaded(): void {
    this._preparing = false;
  }

  loadData(): void{
    this
      .dataService
      .getPositions()
      .subscribe(
        (positions: Position[]) => {
          this._data = positions;
          //this.widgetLoaded();
          //this._dataLoaded = true;
          this._dcService.load(this._config, this._data, this.widgetLoaded.bind(this));
          this._dataLoaded = true;
        },
        (errResp) => {
          console.error('error', errResp);

        }
      );
  }
}
