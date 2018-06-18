/**
 * Created by xn01598 on 11.05.2017.
 */
import { Component } from '@angular/core';
import {Instrument} from "../shared/models/instrument";
import {MyFinanceService} from "../modules/myfinance-tsclient-generated/api/myFinance.service";

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
