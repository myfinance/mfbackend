import { Component, Input, EventEmitter, Output, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import {Instrument} from "../entities/instrument";


@Component({
  selector: 'instrument-card',
  templateUrl: './instrument-card.component.html'
})
export class InstrumentCardComponent {

  @Input() item: Instrument;

  constructor() {
    console.debug('ctor', this.item);
  }




}
