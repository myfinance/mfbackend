import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {UUID} from "angular2-uuid";
import {Subject} from "rxjs/Subject";
import {DcService} from "../../../modules/widget/services/dc.service";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";
import {timeParse} from "d3-time-format";
import {Position} from "../../../shared/models/position";

const d3ParseDateDimension = timeParse('%Y-%m-%d');

@Component({
  selector: 'app-linechartexp',
  templateUrl: './linechartexp.component.html',
  styleUrls: ['./linechartexp.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LinechartexpComponent  implements OnInit {

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
      { id: '81e6ea16-6cba-40a0-8204-0e1f8f103ca5', value: [ 'isin' ] },
      { id: '5e9c4eb3-8526-42f3-9ea8-091148b2dd92', value: [ 'valdate' ] },
      { id: '5304029d-3996-4aff-bafc-11098e3f3d2b', value: [ 'isin', 'valdate' ] }
    ]
  };

  private _widgetConfig= {
    title: 'Positions',
    dimension: '5e9c4eb3-8526-42f3-9ea8-091148b2dd92',
    xAxisFormat: 'date',
    yAxisFormat: 'financial-number',
    lines: [
      {
        title: 'Gesamt',
        group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.price * d.amount),
        color: 'blue',
        tooltip: [
          { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
        ]
      },
      {
        title: 'testinstrument1',
        group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.isin === 'isin00000001' ? (d.price * d.amount): 0),
        color: 'red',
        tooltip: [
          { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
        ]
      },
      {
        title: 'testinstrument2',
        group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => (d.isin === 'isin00000002') ? (d.price * d.amount) : 0),
        color: 'green',
        tooltip: [
          { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
        ]
      }
    ],
    uuid: UUID.UUID()
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
