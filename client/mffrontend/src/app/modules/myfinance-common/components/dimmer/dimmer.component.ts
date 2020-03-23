import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-dimmer',
  templateUrl: './dimmer.component.html',
  styleUrls: ['./dimmer.component.scss']
})
export class DimmerComponent implements OnInit {

  @Input()
  isDimmed


  constructor() { }

  ngOnInit() {
  }

}
