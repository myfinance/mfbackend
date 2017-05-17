/**
 * Created by xn01598 on 11.05.2017.
 */
import { Component } from '@angular/core';
import {Http} from "@angular/http";
import {Instrument} from "../entities/instrument";
import {InstrumentService} from "./instrument.service";

@Component({
  selector: 'instrument-search',
  templateUrl: './instrument-search.html'
})
export class InstrumentSearchComponent {
  isin: string;
  instruments: Array<Instrument>;

  constructor(private instrumentService: InstrumentService) { }
  search(): void{
    this
      .instrumentService
      .find()
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
