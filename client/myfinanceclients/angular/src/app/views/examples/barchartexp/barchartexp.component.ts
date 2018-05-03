import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Subject} from "rxjs/Subject";
import {DcService} from "../../../modules/widget/services/dc.service";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";
import {UUID} from "angular2-uuid";
import {Position} from "../../../shared/models/position";


@Component({
  selector: 'app-barchartexp',
  templateUrl: './barchartexp.component.html',
  styleUrls: ['./barchartexp.component.scss']
})
export class BarchartexpComponent implements OnInit {

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
    dimension: '5304029d-3996-4aff-bafc-11098e3f3d2b',
    group: dimension => dimension.group().reduceSum(d => d.price * d.amount),
    xAxisFormat: 'financial-number',
    tooltip: [
      { key: 'typ', value: 'diff', valueType: 'financial-number' }
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
