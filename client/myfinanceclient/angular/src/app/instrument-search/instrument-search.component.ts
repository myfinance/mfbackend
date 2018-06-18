/**
 * Created by xn01598 on 11.05.2017.
 */
import { Component } from '@angular/core';
import {Instrument, MyFinanceService} from "../modules/myfinance-tsclient-generated";
import {InstrumentListModel} from "../modules/myfinance-tsclient-generated";

@Component({
  selector: 'instrument-search',
  templateUrl: './instrument-search.html'
})
export class InstrumentSearchComponent {
  isin: string;
  instruments: Array<Instrument>;

  constructor(private instrumentService: MyFinanceService) { }
  search(): void{
    this
      .instrumentService.getInstruments_envID('dev')
      .subscribe(
        (instruments: InstrumentListModel) => {
          this.instruments = instruments.values;
        },
        (errResp) => {
          console.error('error', errResp);

        }
      );
  }
  select(instrument: Instrument): void{

  }
}
