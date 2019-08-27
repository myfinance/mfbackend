import { Component, OnInit, OnDestroy, AfterViewInit, Input, ViewChild, ElementRef } from '@angular/core';
import { Subject } from 'rxjs/Subject'
import * as dc from 'dc';

import { DcService } from '../../services/dc.service';
import { FinancialNumberPipe } from '../../pipes/financial-number.pipe';
import {DashboardGridComponent} from "../../../dashboard/components/dashboard-grid/dashboard-grid.component";

@Component({
  selector: 'app-widget-horizontal-bar-chart',
  templateUrl: './widget-horizontal-bar-chart.component.html',
  styleUrls: ['./widget-horizontal-bar-chart.component.scss']
})
export class WidgetHorizontalBarChartComponent implements OnInit, OnDestroy, AfterViewInit {

  private _chart;


  @Input()
  hideHeader

  @Input()
  config


  @Input()
  resized: Subject<any>;

  @ViewChild('chart', {static: false}) chart: ElementRef;

  constructor(private _dcService: DcService) { }

  ngOnInit() {
    this.resized.subscribe(uuid => {
      if(this.config.uuid == uuid) {
        this.resize();
      }
    })
  }

  ngAfterViewInit() {
    this._chart = dc.rowChart('#chart-' + this.config.uuid)
      .dimension(this._dcService.getDimension(this.config.dimension))
      .group(this.config.group(this._dcService.getDimension(this.config.dimension)))
      .elasticX(true)
      .controlsUseVisibility(true)
      .width(this.chart.nativeElement.offsetWidth)
      .height(this.chart.nativeElement.offsetHeight - 40);

    if(this.config.tooltip) {
      let tooltip = (d) => {
        let text = "";
        for(let tooltip of this.config.tooltip) {
          let keySelector;
          if(tooltip.name) {
            keySelector = tooltip.name;
          } else {
            keySelector = d.key;
          }

          if(tooltip.valueType && tooltip.valueType == 'financial-number') {
            let pipe = new FinancialNumberPipe();
            text += `${keySelector}: ${pipe.transform(d.value)}\n`;
          } else {
            text += `${keySelector}: ${d.value}\n`;
          }
        }

        return text;
      }

      this._chart.title(tooltip);
    }

    if(this.config.xAxisFormat == 'financial-number') {
      let pipe = new FinancialNumberPipe();
      this._chart.xAxis().tickFormat(d => pipe.transform(d));
    }

    this._chart.render();
  }

  resize(): void {
    this._chart.width(this.chart.nativeElement.offsetWidth);
    this._chart.height(this.chart.nativeElement.offsetHeight - 40);
    this._chart.redraw();
  }

  reset(): void {
    this._chart.filterAll();
    dc.redrawAll();
  }

  ngOnDestroy() {
    this.resized.unsubscribe();
  }

}
