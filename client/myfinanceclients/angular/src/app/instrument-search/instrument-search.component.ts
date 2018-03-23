/**
 * Created by xn01598 on 11.05.2017.
 */
import { Component } from '@angular/core';
import {Instrument} from "../shared/models/instrument";
import {MyFinanceDataService} from "../shared/services/myfinance-data.service";

@Component({
  selector: 'instrument-search',
  templateUrl: './instrument-search.html'
})
export class InstrumentSearchComponent {
  isin: string;
  instruments: Array<Instrument>;

  constructor(private instrumentService: MyFinanceDataService) { }
  search(): void{
    this
      .instrumentService
      .find(this.isin)
      .subscribe(
        (instruments: Instrument[]) => {
          this.instruments = instruments;
        },
        (errResp) => {
          console.error('error', errResp);

        }
      );
  }
  select(instrument: Instrument): void{

  }
}
