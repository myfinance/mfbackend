import { Component, OnInit, OnDestroy, AfterViewInit, Input, ViewChild, ElementRef } from '@angular/core';
import { Subject } from 'rxjs/Subject'
import dc from 'dc';

import { DcService } from '../../services/dc.service';
import { FinancialNumberPipe } from '../../pipes/financial-number.pipe';

@Component({
  selector: 'app-widget-horizontal-bar-chart',
  templateUrl: './widget-horizontal-bar-chart.component.html',
  styleUrls: ['./widget-horizontal-bar-chart.component.scss']
})
export class WidgetHorizontalBarChartComponent implements OnInit, OnDestroy, AfterViewInit {

  private _config;
  private _chart;
  private _hideHeader;

  @Input()
  set config(config) {
    this._config = config;
  }
  get config() {
    return this._config;
  }

  @Input()
  set hideHeader(hideHeader: boolean) {
    this._hideHeader = hideHeader;
  }
  get hideHeader() {
    return this._hideHeader;
  }

  @Input()
  resized: Subject<any>;

  @ViewChild('chart') chart: ElementRef;

  constructor(private _dcService: DcService) { }

  ngOnInit() {
    this.resized.subscribe(uuid => {
      if(this._config.uuid == uuid) {
        this.resize();
      }
    })
  }

  ngAfterViewInit() {
    this._chart = dc.rowChart('#chart-' + this._config.uuid)
      .dimension(this._dcService.getDimension(this._config.dimension))
      .group(this._config.group(this._dcService.getDimension(this._config.dimension)))
      .elasticX(true)
      .controlsUseVisibility(true)
      .width(this.chart.nativeElement.offsetWidth)
      .height(this.chart.nativeElement.offsetHeight - 40);

    if(this._config.tooltip) {
      let tooltip = (d) => {
        let text = "";
        for(let tooltip of this._config.tooltip) {
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

    if(this._config.xAxisFormat == 'financial-number') {
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
