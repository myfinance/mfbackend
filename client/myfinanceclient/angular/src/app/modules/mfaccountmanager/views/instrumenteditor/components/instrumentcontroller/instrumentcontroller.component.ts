import {Component, OnDestroy, OnInit} from '@angular/core';
import {Instrument} from '../../../../../myfinance-tsclient-generated';
import {InstrumentService} from '../../services/instrument.service';

@Component({
  selector: 'app-instrumentcontroller',
  templateUrl: './instrumentcontroller.component.html',
  styleUrls: ['./instrumentcontroller.component.scss']
})
export class InstrumentcontrollerComponent implements OnInit, OnDestroy {

  noInstrumentSelected = true;
  selectedInstrument: Instrument

  constructor(private instrumentservice: InstrumentService) { }

  ngOnInit() {
    this.instrumentservice.selectedinstrumentSubject.subscribe(
      () => {
        this.updateSelectedTenant()
      }
    )
  }

  updateSelectedTenant() {
    console.log('updateSelectedTenant')
    this.selectedInstrument = this.instrumentservice.getSelectedInstrument()
    if (this.selectedInstrument) { this.noInstrumentSelected = false; }
  }

  getSelectedInstrumentId(): number {
    if (!this.selectedInstrument) { return 0; } else { return this.selectedInstrument.instrumentid; }
  }

  ngOnDestroy(): void {
  }
}
