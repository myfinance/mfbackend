import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SuiModule } from 'ng2-semantic-ui';

import { DcService } from './services/dc.service';
import { WidgetHorizontalBarChartComponent } from './components/widget-horizontal-bar-chart/widget-horizontal-bar-chart.component';
import { FinancialNumberPipe } from './pipes/financial-number.pipe';
import { WidgetCompositeLineChartComponent } from './components/widget-composite-line-chart/widget-composite-line-chart.component';
import {WidgetContentComponent} from "./shared/component/widget-content/widget-content.component";

@NgModule({
  imports: [
    CommonModule,
    SuiModule
  ],
  declarations: [
    WidgetHorizontalBarChartComponent,
    WidgetCompositeLineChartComponent,
    WidgetContentComponent,
    FinancialNumberPipe
  ],
  exports: [
    WidgetHorizontalBarChartComponent,
    WidgetCompositeLineChartComponent
  ],
  providers: [
    DcService
  ]
})
export class WidgetModule { }
