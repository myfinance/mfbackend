import { Component, OnInit } from '@angular/core';
import {BsDatepickerConfig} from "ngx-bootstrap";

@Component({
  selector: 'app-controller',
  templateUrl: './controller.component.html',
  styleUrls: ['./controller.component.scss']
})
export class ControllerComponent implements OnInit {

  bsConfig: Partial<BsDatepickerConfig>;
  daterangepickerModel: Date[];

  constructor() { }

  ngOnInit() {
    this.bsConfig = Object.assign({}, { containerClass: 'theme-default', rangeInputFormat: 'YYYY-MM-DD',  });
  }

}
