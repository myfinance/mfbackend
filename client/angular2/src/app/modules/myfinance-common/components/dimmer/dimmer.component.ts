import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-dimmer',
  templateUrl: './dimmer.component.html',
  styleUrls: ['./dimmer.component.scss']
})
export class DimmerComponent implements OnInit {

  private _isDimmed: boolean = false;

  @Input()
  set isDimmed(isDimmed: boolean) {
    this._isDimmed = isDimmed;
  }
  get isDimmed() {
    return this._isDimmed;
  }

  constructor() { }

  ngOnInit() {
  }

}
