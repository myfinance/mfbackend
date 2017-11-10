import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs/Subject";
import {DcService} from "../../../modules/widget/services/dc.service";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";


@Component({
  selector: 'app-barchartexp',
  templateUrl: './barchartexp.component.html',
  styleUrls: ['./barchartexp.component.scss']
})
export class BarchartexpComponent implements OnInit {

  private _loading: boolean = false;
  private _preparing: boolean = false;
  private _dataLoaded: boolean = false;

    private _config= {
    title: 'Positions',
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
      { id: '5304029d-3996-4aff-bafc-11098e3f3d2b', value: [ '' ] }
    ],
    dimension: '81e6ea16-6cba-40a0-8204-0e1f8f103ca5',
    group: dimension => dimension.group().reduceSum(d => d.diff)
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
