import { Component, OnInit, OnDestroy, AfterViewInit, Input, ViewChild, ElementRef } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Subject } from 'rxjs/Subject'
import * as dc from 'dc';
import { timeFormat } from 'd3-time-format';

import { DcService } from '../../services/dc.service';
import { FinancialNumberPipe } from '../../pipes/financial-number.pipe';

@Component({
  selector: 'app-widget-composite-line-chart',
  templateUrl: './widget-composite-line-chart.component.html',
  styleUrls: ['./widget-composite-line-chart.component.scss']
})
export class WidgetCompositeLineChartComponent implements OnInit, OnDestroy, AfterViewInit {

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
    this._chart = dc.compositeChart('#chart-' + this.config.uuid)
      .width(this.chart.nativeElement.offsetWidth)
      .height(this.chart.nativeElement.offsetHeight - 40)
      .elasticX(true)
      .elasticY(true)
      .legend(dc.legend().x(60).y(10).itemHeight(13).gap(5))
      .margins({top: 10, right: 20, bottom: 25, left: 55})
      .controlsUseVisibility(true)
      .renderHorizontalGridLines(true)
      .shareTitle(false)

    this._chart
      .compose(
        this.config.lines.map(
          function(line, idx){
            let c = dc.lineChart(this._chart)
              .dimension(this._dcService.getDimension(this.config.dimension))
              .group(line.group(this._dcService.getDimension(this.config.dimension)), line.title)
              .colors(line.color)

            if(this.config.tooltip) c.title(this._buildTooltip(this.config.tooltip));

            return c;
          }, this
        )
      )
      .brushOn(false);

    switch(this.config.xAxisFormat) {
      case 'financial-number': {
        let pipe = new FinancialNumberPipe();
        this._chart.xAxis().tickFormat(d => pipe.transform(d));
        break;
      }
      case 'date': {
        this._chart
        let pipe = new DatePipe('de-DE');
        this._chart.xAxis().ticks(15).tickFormat(timeFormat('%d.%m.%Y'));
        break;
      }
      default: break;
    }

    switch(this.config.yAxisFormat) {
      case 'financial-number': {
        let pipe = new FinancialNumberPipe();
        this._chart.yAxis().tickFormat(d => pipe.transform(d));
        break;
      }
      case 'date': {
        let pipe = new DatePipe('de-DE');
        this._chart.yAxis().ticks(15).tickFormat(d => pipe.transform(d));
        break;
      }
      default: break;
    }

    this._chart.render();
  }

  // d => `${germanTimeFormat(d.key)}: ${financialNumberFormat(d.value)}`
  private _buildTooltip(config: any): (d: any) => string {
    let tooltip = (d) => {
      let text = "";
      for(let tooltip of config.tooltip) {
        let keySelector;
        if(tooltip.name) {
          keySelector = tooltip.name;
        } else {
          if(tooltip.keyType && tooltip.keyType == 'date') {
            let pipe = new DatePipe('de-DE');
            keySelector = pipe.transform(d.key);
          } else {
            keySelector = d.key;
          }
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

    return tooltip;
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
