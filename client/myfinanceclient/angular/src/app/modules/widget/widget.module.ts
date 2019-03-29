import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DcService } from './services/dc.service';
import { WidgetHorizontalBarChartComponent } from './components/widget-horizontal-bar-chart/widget-horizontal-bar-chart.component';
import { FinancialNumberPipe } from './pipes/financial-number.pipe';
import { WidgetCompositeLineChartComponent } from './components/widget-composite-line-chart/widget-composite-line-chart.component';
import {WidgetContentComponent} from "./shared/component/widget-content/widget-content.component";
import {WidgetDataTableComponent} from "./components/widget-data-table/widget-data-table.component";
import {AgGridModule} from "ag-grid-angular";
import {BooleanPipe} from "./pipes/boolean.pipe";

@NgModule({
  imports: [
    CommonModule,
    AgGridModule.withComponents([])
  ],
  declarations: [
    WidgetHorizontalBarChartComponent,
    WidgetCompositeLineChartComponent,
    WidgetContentComponent,
    WidgetDataTableComponent,
    FinancialNumberPipe,
    BooleanPipe
  ],
  exports: [
    WidgetHorizontalBarChartComponent,
    WidgetDataTableComponent,
    WidgetCompositeLineChartComponent
  ],
  providers: [
    DcService
  ]
})
export class WidgetModule { }
